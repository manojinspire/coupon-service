package com.example.coupons.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "buy_items")
@Data
@NoArgsConstructor
public class BuyItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private BXGYCoupon coupon;

    private String productId;

    private String categoryId;

    private Integer minQuantity;
}