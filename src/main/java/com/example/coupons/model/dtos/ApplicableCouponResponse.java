package com.example.coupons.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicableCouponResponse {
    private Long couponId;
    private String couponCode;
    private String type;
    private Double discountAmount;
    private Double cashbackAmount;
    private Boolean isApplicable;
    private String message;
    private Map<String, Double> itemDiscounts;
}
