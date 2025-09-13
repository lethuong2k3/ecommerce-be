package net.fpoly.ecommerce.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTrackingRequest {
    private String keyword;
    private Instant startDate;
    private Instant endDate;
    private String type;
    private String sortBy;
    private int size;
    private int page;
}
