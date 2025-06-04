package net.fpoly.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import net.fpoly.ecommerce.config.Environment;
import net.fpoly.ecommerce.constants.Parameter;
import net.fpoly.ecommerce.model.momo.QueryStatusTransactionResponse;
import net.fpoly.ecommerce.service.impl.momo.QueryTransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class MoMoController {

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

    @GetMapping("/user/api/momo/notify")
    public Integer paymentNotify(@RequestParam Map<String, String> request) {
        Integer resultCode = Integer.valueOf(request.get(Parameter.RESULT_CODE));
        return resultCode;
    }

}
