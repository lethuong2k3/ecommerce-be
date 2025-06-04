package net.fpoly.ecommerce.constants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUser {
    private String email;

    private String verificationCode;
}
