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
public class ApplyCouponResponse {
    private CartDto updatedCart;
    private Double totalDiscount;
    private Double cashbackAmount;
    private Map<String, Double> itemDiscounts;
    private String message;
    private Boolean success;
}
