package com.example.coupons.model.entities;

import com.example.coupons.enums.DiscountScope;
import com.example.coupons.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "discount_coupons")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DiscountCoupon extends Coupon {

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private Double discountValue;

    private Double maxDiscountAmount;

    @Enumerated(EnumType.STRING)
    private DiscountScope discountScope;

    @Column(length = 1000)
    private String applicableProductIds;

    @Column(length = 1000)
    private String applicableCategoryIds;
}
