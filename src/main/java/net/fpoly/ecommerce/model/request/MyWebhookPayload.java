package net.fpoly.ecommerce.model.request;

import lombok.Data;
import vn.payos.type.WebhookData;


@Data
public class MyWebhookPayload {
    public String code;
    public String desc;
    public WebhookData data;
    public String signature;
}
