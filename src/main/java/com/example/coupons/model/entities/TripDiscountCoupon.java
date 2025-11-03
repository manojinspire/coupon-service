package com.example.coupons.model.entities;


import com.example.coupons.enums.DiscountType;
import com.example.coupons.enums.TripType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trip_discount_coupons")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TripDiscountCoupon extends Coupon {

    @Enumerated(EnumType.STRING)
    private TripType tripType;

    private Double discountValue;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(length = 1000)
    private String applicableRoutes;

    @Column(length = 500)
    private String applicableVehicleTypes;

    private Integer minTripDistance;

    private Double minTripAmount;
}

