package net.fpoly.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import net.fpoly.ecommerce.config.Environment;
import net.fpoly.ecommerce.exception.InsufficientStockException;
import net.fpoly.ecommerce.model.*;
import net.fpoly.ecommerce.model.momo.PaymentResponse;
import net.fpoly.ecommerce.model.momo.RequestType;
import net.fpoly.ecommerce.model.request.OrderRequest;
import net.fpoly.ecommerce.model.response.OrderResponse;
import net.fpoly.ecommerce.repository.*;
import net.fpoly.ecommerce.service.OrderService;
import net.fpoly.ecommerce.service.PaymentService;
import net.fpoly.ecommerce.service.ShipmentService;
import net.fpoly.ecommerce.service.impl.momo.CreateOrderMoMo;
import net.fpoly.ecommerce.util.LogUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
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

    private BigDecimal totalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getItemPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Integer checkQuantity(Integer quantity, ProductDetail productDetail) {
         ProductDetail productDetail1 = productDetailRepo.findById(productDetail.getId()).orElseThrow(() -> new IllegalArgumentException("Product detail not found"));
        if (quantity > productDetail1.getAmount()) {
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
                    .orderDate(new Date())
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

    private PaymentResponse handleMoMoPayment(Order order, Users user, PaymentType paymentType ,OrderRequest orderRequest) throws Exception {
        LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
        Long transId = 2L;
        order.setOrderStatus(OrderStatus.WAITING);
        order.setOrderDate(new Date());
        order.setPayment(paymentService.createPayment(paymentType, user, order.getTotalAmount()));
        order.setShipment(shipmentService.createShipment(orderRequest.getShipmentRequest(), user));
        String partnerClientId = "partnerClientId";
        String orderInfo = "Pay with MoMo";
        String returnURL = "http://localhost:8080/user/api/momo/payment";
        String notifyURL = "http://localhost:8080/user/api/momo/notify";
        Environment environment = Environment.selectEnv("dev");
        PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(order.getTotalAmount().longValue()),  orderInfo, returnURL, notifyURL, "", RequestType.PAY_WITH_ATM, Boolean.TRUE);
        deductStockForOrderItems(order);
        orderRepo.save(order);
        return captureWalletMoMoResponse;
    }

    private void deductStockForOrderItems(Order order) {
        order.getOrderItems().forEach(item -> {
            ProductDetail productDetail = item.getProductDetail();
            if (productDetail.getAmount() < item.getQuantity()) {
                throw new IllegalArgumentException("Sản phẩm " + productDetail.getProduct().getName() + " không đủ số lượng tồn");
            }
            productDetail.setAmount(productDetail.getAmount() - item.getQuantity());
            productDetailRepo.save(productDetail);
        });
    }

    @Override
    public Object updateOrder(OrderRequest orderRequest, Principal principal) throws Exception {
        Users user = userRepo.findByEmail(principal.getName());
        Order order = orderRepo.findByUserAndOrderStatus(user, OrderStatus.CART);
        PaymentType paymentType = paymentTypeRepo.findById(orderRequest.getPaymentTypeId()).orElseThrow(() -> new IllegalArgumentException("Payment type not found"));
        switch (paymentType.getValue()) {
            case "COD":
                order.setOrderDate(new Date());
                order.setOrderStatus(OrderStatus.PENDING);
                order.setPayment(paymentService.createPayment(paymentType, user, order.getTotalAmount()));
                order.setShipment(shipmentService.createShipment(orderRequest.getShipmentRequest(), user));
                deductStockForOrderItems(order);
                return OrderResponse.convertToOrderResponse(orderRepo.save(order));
            case "MoMo":
               return handleMoMoPayment(order, user, paymentType, orderRequest);
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
        return OrderResponse.convertToOrderResponse(order);
    }

    @Override
    public OrderResponse restoreStockForOrderItems(Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());

        return null;
    }

}
