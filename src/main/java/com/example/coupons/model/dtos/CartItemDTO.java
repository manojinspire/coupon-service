package com.example.coupons.model.dtos;

import lombok.Data;

@Data
public class CartItemDTO {
    private String productId;
    private String productName;
    private String categoryId;
    private Double price;
    private Integer quantity;
    private Double discount = 0.0;
    private Boolean isFree = false;

    public double getItemTotal() {
        return price * quantity - discount;
    }
}
