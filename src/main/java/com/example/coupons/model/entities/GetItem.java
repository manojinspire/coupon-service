package com.example.coupons.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "get_items")
@Data
@NoArgsConstructor
public class GetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private BXGYCoupon coupon;

    private String productId;

    private String categoryId;

    private Integer maxQuantity;

    private Integer priority = 0;
}
