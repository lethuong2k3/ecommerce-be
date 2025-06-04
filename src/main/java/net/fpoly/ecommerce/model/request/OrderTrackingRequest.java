package net.fpoly.ecommerce.model.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTrackingRequest {
    private String keyword;
    private Date startDate;
    private Date endDate;
    private String type;
    private String sortBy;
    private int size;
    private int page;
}
