package com.example.coupons.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResult {
    private String couponCode;
    private Double discountAmount;
    private Double cashbackAmount;
    private List<String> freeItemIds;
    @Builder.Default
    private List<String> errors = new ArrayList<>();
    private String message;
    private Boolean success;

    public static DiscountResult failed(String error) {
        return DiscountResult.builder()
                .success(false)
                .errors(List.of(error))
                .message(error)
                .build();
    }

    public static DiscountResult success(String message) {
        return DiscountResult.builder()
                .success(true)
                .message(message)
                .freeItemIds(new ArrayList<>())
                .build();
    }
}
