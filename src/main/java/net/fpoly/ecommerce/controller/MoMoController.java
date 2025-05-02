package net.fpoly.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import net.fpoly.ecommerce.constants.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MoMoController {

    @GetMapping("/user/api/momo/payment")
    public String paymentSuccess(@RequestParam Map<String, String> request) {
        Integer resultCode = Integer.valueOf(request.get(Parameter.RESULT_CODE));
        return resultCode == 0 ? "Giao dich thanh cong" : "Giao dich that bai";
    }

    @GetMapping("/user/api/momo/notify")
    public Integer paymentNotify(@RequestParam Map<String, String> request) {
        Integer resultCode = Integer.valueOf(request.get(Parameter.RESULT_CODE));
        return resultCode;
    }

}
