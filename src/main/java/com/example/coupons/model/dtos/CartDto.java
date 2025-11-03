package com.example.coupons.model.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private String userId;
    private String userType;
    private List<CartItemDTO> items;
    private String paymentMethod;
    private LocationDTO location;
    private TripDetailsDTO tripDetails;
    private List<String> appliedCouponCodes;

    public double calculateSubtotal() {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
