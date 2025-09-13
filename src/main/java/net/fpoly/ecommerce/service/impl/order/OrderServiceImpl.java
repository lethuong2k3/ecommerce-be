package net.fpoly.ecommerce.service.impl.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.fpoly.ecommerce.exception.InsufficientStockException;
import net.fpoly.ecommerce.model.*;
import net.fpoly.ecommerce.model.request.OrderRequest;
import net.fpoly.ecommerce.model.request.OrderTrackingRequest;
import net.fpoly.ecommerce.model.response.OrderResponse;
import net.fpoly.ecommerce.repository.*;
import net.fpoly.ecommerce.service.OrderHistoryService;
import net.fpoly.ecommerce.service.OrderService;
import net.fpoly.ecommerce.service.PaymentService;
import net.fpoly.ecommerce.service.ShipmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;



@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;

    private final UserRepo userRepo;

    private final OrderItemRepo orderItemRepo;

    private final ProductDetailRepo productDetailRepo;

    private final PaymentTypeRepo paymentTypeRepo;

    private final PaymentService paymentService;

    private final ShipmentService shipmentService;

    private final OrderHistoryService orderHistoryService;

    private final PayOS payOS;

    @Value("${frontend.url}")
    private String frontEndUrl;

    @Value("${reservation.ttl-minutes}")
    private long ttlMinutes;

    @Value("${reservation.ttl-hours}")
    private long ttlHours;

    private BigDecimal totalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getItemPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Integer checkQuantity(Integer quantity, ProductDetail productDetail) {
         ProductDetail productDetail1 = productDetailRepo.findById(productDetail.getId()).orElseThrow(() -> new IllegalArgumentException("Product detail not found"));
        if (quantity > productDetail1.getStockAvailable()) {
            throw new InsufficientStockException("Product name " + productDetail1.getProduct().getName() + "is out of stock");
        }
        return quantity;
    }

    @Override
    public Order createOrder(OrderRequest orderRequest, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        Order order = orderRepo.findByUserAndOrderStatus(user, OrderStatus.CART);
        List<OrderItem> orderItems = orderRequest.getOrderItems();

        orderItems.forEach(item -> {
            item.setItemPrice(item.getProductDetail().getPrice());
            item.setQuantity(checkQuantity(item.getQuantity(), item.getProductDetail()));
            item.setTotalPrice(item.getItemPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setOrders(order);
        });

        if (order == null) {
            Order newOrder = Order.builder()
                    .user(user)
                    .orderStatus(OrderStatus.CART)
                    .orderItems(orderItems)
                    .totalAmount(totalAmount(orderItems))
                    .build();
            orderItems.forEach(item -> item.setOrders(newOrder));
            orderRepo.save(newOrder);
            return newOrder;
        }

        OrderItem existingItem = orderItemRepo.findByOrders_IdAndProductDetail_Id(order.getId(), orderItems.get(0).getProductDetail().getId());
        if (existingItem != null) {
            existingItem.setQuantity(checkQuantity(existingItem.getQuantity() + orderItems.get(0).getQuantity(), existingItem.getProductDetail()));
            existingItem.setTotalPrice(existingItem.getItemPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            order.getOrderItems().add(existingItem);
        } else {
            orderItems.get(0).setOrders(order);
            order.getOrderItems().add(orderItems.get(0));
        }

        order.setTotalAmount(totalAmount(order.getOrderItems()));
        return orderRepo.save(order);
    }

    public Object handlePayOSPayment(Order order, PaymentType paymentType, Users user,OrderRequest orderRequest) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            String returnUrl = frontEndUrl + "/thanh-toan/ket-qua";
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
            List<ItemData> itemDataList = order.getOrderItems().stream()
                    .map(orderItem -> ItemData.builder()
                            .name(orderItem.getProductDetail().getProduct().getName())
                            .price(orderItem.getItemPrice().intValue())
                            .quantity(orderItem.getQuantity())
                            .build())
                    .toList();
            PaymentData paymentData = PaymentData.builder().orderCode(orderCode).description("Thanh toan don hang").amount(order.getTotalAmount().intValue() + orderRequest.getShippingFee().intValue())
                    .items(itemDataList).returnUrl(returnUrl).cancelUrl(returnUrl).build();
            CheckoutResponseData data = payOS.createPaymentLink(paymentData);
            order.setOrderStatus(OrderStatus.WAITING);
            order.setOrderDate(Instant.now());
            order.setExpiresAt(Instant.now().plus(ttlMinutes, ChronoUnit.MINUTES));
            order.setOrderCode(data.getOrderCode());
            order.setPayment(paymentService.createPayment(paymentType, user, order.getTotalAmount()));
            order.setShipment(shipmentService.createShipment(orderRequest.getShipmentRequest(), user));
            order.setShippingFee(orderRequest.getShippingFee());
            order.getOrderHistories().addAll(List.of(
                    setOrderHistory(order, OrderStatus.PENDING, Instant.now(), user.getName()),
                    setOrderHistory(order, OrderStatus.WAITING, Instant.now(), user.getName())
            ));
            var saveOrder = orderRepo.save(order);
            handleStockReserve(saveOrder);
            response.put("error", 0);
            response.put("message", "success");
            response.set("data", objectMapper.valueToTree(data));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", "fail");
            response.set("data", null);
            return response;
        }
    }

    public void deductStockForOrderItems(Order order) {
        order.getOrderItems().forEach(item -> {
            ProductDetail productDetail = item.getProductDetail();
            if (productDetail.getAmount() < item.getQuantity()) {
                throw new IllegalArgumentException("Sản phẩm " + productDetail.getProduct().getName() + " không đủ số lượng tồn");
            }
            productDetail.setAmount(productDetail.getAmount() - item.getQuantity());
            productDetailRepo.save(productDetail);
        });
    }

    public void handleStockReserve(Order order) {
        for (var item : order.getOrderItems()) {
            var productDetail = productDetailRepo.findByIdForUpdate(item.getProductDetail().getId()).orElseThrow(() -> new EntityNotFoundException("Product not found: " + item.getProductDetail().getId()));
            int available = productDetail.getStockAvailable();
            if (available < item.getQuantity()) {
                throw new IllegalStateException("Not enough stock for product " + productDetail.getId() +
                        ": required=" + item.getQuantity() + ", available=" + available);
            }
            productDetail.setStockReserved(productDetail.getStockReserved() + item.getQuantity());
            productDetailRepo.save(productDetail);
        }
    }

    private OrderHistory setOrderHistory(Order order, OrderStatus orderStatus, Instant orderDate, String userName) {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setStatus(orderStatus);
        orderHistory.setChangedAt(orderDate);
        orderHistory.setChangedBy(userName);
        return orderHistory;
    }


    @Override
    @Transactional
    public Object updateOrder(OrderRequest orderRequest, Principal principal) throws Exception {
        Users user = userRepo.findByEmail(principal.getName());
        Order order = orderRepo.findByUserAndOrderStatus(user, OrderStatus.CART);
        PaymentType paymentType = paymentTypeRepo.findById(orderRequest.getPaymentTypeId()).orElseThrow(() -> new IllegalArgumentException("Payment type not found"));
        switch (paymentType.getValue()) {
            case "COD":
                order.setOrderDate(Instant.now());
                order.setExpiresAt(Instant.now().plus(ttlHours, ChronoUnit.HOURS));
                order.setOrderStatus(OrderStatus.PENDING);
                order.setPayment(paymentService.createPayment(paymentType, user, order.getTotalAmount()));
                order.setShipment(shipmentService.createShipment(orderRequest.getShipmentRequest(), user));
                order.setShippingFee(orderRequest.getShippingFee());
                order.getOrderHistories().add(setOrderHistory(order, OrderStatus.PENDING, Instant.now(), principal.getName()));
                deductStockForOrderItems(order);
                return OrderResponse.convertToOrderResponse(orderRepo.save(order));
            case "VietQR":
               return handlePayOSPayment(order, paymentType, user, orderRequest);
            default:
                throw new IllegalArgumentException("Unsupported payment type: " + paymentType.getValue());
        }
    }


    @Override
    public OrderResponse findByUserAndOrderStatus(Principal principal, OrderStatus orderStatus) {
        Users user = userRepo.findByEmail(principal.getName());
        Order order = orderRepo.findByUserAndOrderStatus(user, orderStatus);
        if (order == null) {
            return null;
        }
        return OrderResponse.convertToCartResponse(order);
    }

    @Override
    public Page<OrderResponse> findByKeywordAndBetweenDate(OrderTrackingRequest request, Principal principal) {
        Sort sort = request.getType().equals("desc") ? Sort.by(request.getSortBy()).descending() : Sort.by(request.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Users user = userRepo.findByEmail(principal.getName());
        Page<Order> orders = orderRepo.findByKeywordAndBetweenDate(request.getKeyword(), request.getStartDate(), request.getEndDate(), user, pageable);
        return orders.map(OrderResponse::convertToOrderResponse);
    }

    @Override
    public Optional<Order> findByOrderCode(Long orderCode) {
        return orderRepo.findByOrderCode(orderCode);
    }

    private void confirmPayment(Order order) throws Exception {
        if (order.getOrderStatus() != OrderStatus.WAITING) {
            return;
        }
        if (order.getExpiresAt().isBefore(Instant.now())) {
            payOS.cancelPaymentLink(order.getOrderCode().intValue(), null);
            cancelOrder(order, "System");
            throw new IllegalArgumentException("Order expired; cannot confirm payment");
        }
        order.getOrderItems().forEach(oi -> {
            var product = productDetailRepo.findByIdForUpdate(oi.getProductDetail().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + oi.getProductDetail().getId()));
            product.setStockReserved(product.getStockReserved() - oi.getQuantity());
            product.setAmount(product.getAmount() - oi.getQuantity());
        });
        order.setOrderStatus(OrderStatus.PAID);
        order.getOrderHistories().add(setOrderHistory(order, OrderStatus.PENDING, Instant.now(), "System")) ;
        orderRepo.save(order);
    }

    @Override
    @Transactional
    public void confirmPaymentByCode(Long orderCode) throws Exception {
        var order = findByOrderCode(orderCode).get();
        confirmPayment(order);
    }

    @Override
    public void cancelOrder(Order order, Principal principal) throws Exception {
        Users user = userRepo.findByEmail(principal.getName());
        internalCancelOrder(order, user.getName());
    }

    public void cancelOrder(Order order, String cancelledBy) throws Exception {
        internalCancelOrder(order, cancelledBy);
    }

    private void internalCancelOrder(Order order, String cancelledBy) throws Exception {
        order.getOrderItems().forEach(oi -> {
            var product = productDetailRepo.findByIdForUpdate(oi.getProductDetail().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + oi.getProductDetail().getId()));
            product.setStockReserved(product.getStockReserved() - oi.getQuantity());
        });

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.getOrderHistories().add(
                setOrderHistory(order, OrderStatus.CANCELLED, Instant.now(), cancelledBy)
        );
        orderRepo.save(order);
    }

    @Override
    @Transactional
    public void expireWaitingOrders() {
        var now = Instant.now();
        var expired = orderRepo.findExpiredWaiting(now);
        for (var order : expired) {
            order.getOrderItems().forEach(oi -> {
                var productDetail = productDetailRepo.findByIdForUpdate(oi.getProductDetail().getId()).orElseThrow(() -> new EntityNotFoundException("Product not found: " + oi.getProductDetail().getId()));
                productDetail.setStockReserved(productDetail.getStockReserved() - oi.getQuantity());
            });
            order.setOrderStatus(OrderStatus.EXPIRED);
            order.getOrderHistories().add(setOrderHistory(order, OrderStatus.EXPIRED, Instant.now(), "System"));
        }
        orderRepo.saveAll(expired);
    }

    @Override
    public OrderResponse orderDetails(Long orderCode, Principal principal) {
        List<OrderHistory> orderHistories = orderHistoryService.getOrderHistory(orderCode, principal);
        Order order = orderRepo.findById(orderCode).orElseThrow(null);
        order.getOrderHistories().addAll(orderHistories);
        return OrderResponse.convertToOrderDetailResponse(order);
    }

}
