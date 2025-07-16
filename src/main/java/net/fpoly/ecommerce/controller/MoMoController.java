package net.fpoly.ecommerce.controller;

import net.fpoly.ecommerce.config.Environment;
import net.fpoly.ecommerce.constants.Parameter;
import net.fpoly.ecommerce.model.Order;
import net.fpoly.ecommerce.model.OrderStatus;
import net.fpoly.ecommerce.model.momo.QueryStatusTransactionResponse;
import net.fpoly.ecommerce.repository.OrderRepo;
import net.fpoly.ecommerce.service.OrderService;
import net.fpoly.ecommerce.service.impl.momo.QueryTransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class MoMoController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepo orderRepo;

    @GetMapping("/user/momo/ipn")
    public ResponseEntity<?> handleMoMoRedirect(@RequestParam String orderId, @RequestParam String requestId) {
        Environment env =  Environment.selectEnv("dev");
        try {
            QueryStatusTransactionResponse response = QueryTransactionStatus.process(env, orderId, requestId);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi gọi MoMo");
            }
            if (response.getResultCode() == 0) {
                return ResponseEntity.ok("Thanh toán thành công cho đơn hàng: " + orderId);
            }
            return ResponseEntity.badRequest().body("Thanh toán thất bại: " + response.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi xử lý: " + e.getMessage());
        }

    }

    @PostMapping("/user/momo/notify")
    public ResponseEntity<String> paymentNotify(@RequestBody Map<String, String> request) {
        System.out.println(request);
        try {
            String orderId = request.get(Parameter.ORDER_ID);
            String requestId = request.get(Parameter.REQUEST_ID);
            Integer resultCode = Integer.valueOf(request.get(Parameter.RESULT_CODE));

            Order order = orderRepo.findByOrderIdAndRequestId(orderId, requestId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đơn hàng");
            }

            order.setOrderStatus(resultCode == 0 ? OrderStatus.PAID : OrderStatus.CANCELLED);
            orderRepo.save(order);

            return ResponseEntity.ok("Thanh toán thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
