package net.fpoly.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fpoly.ecommerce.model.request.MyWebhookPayload;
import net.fpoly.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.type.PaymentLinkData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

import java.security.Principal;

@RestController
public class PayOSController {

    @Autowired
    private PayOS payOS;

    @Autowired
    private OrderService orderService;

    @PutMapping("/payos/{orderId}")
    public ObjectNode cancelOrder(@PathVariable("orderId") int orderId, Principal principal) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            PaymentLinkData order = payOS.cancelPaymentLink(orderId, null);
            orderService.cancelOrder(orderService.findByOrderCode(order.getOrderCode()).get(), principal);
            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @PostMapping("/payos/payos_transfer_handler")
    public ObjectNode payosTransferHandler(@RequestBody MyWebhookPayload body)
            throws JsonProcessingException, IllegalArgumentException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        Webhook webhookBody = Webhook.builder()
                .success("00".equals(body.code))
                .code(body.code)
                .desc(body.desc)
                .data(body.data)
                .signature(body.signature)
                .build();
        try {
            // Init Response
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
            orderService.confirmPaymentByCode(data.getOrderCode());
            response.put("error", 0);
            response.put("status", 200);
            response.set("data", objectMapper.valueToTree(data));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }
}
