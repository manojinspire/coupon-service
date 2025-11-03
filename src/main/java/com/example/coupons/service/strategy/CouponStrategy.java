package com.example.coupons.service.strategy;

import com.example.coupons.model.dtos.CartDto;
import com.example.coupons.model.dtos.DiscountResult;
import com.example.coupons.model.entities.Coupon;

public interface CouponStrategy {
    DiscountResult apply(Coupon coupon, CartDto cart);
    boolean isApplicable(Coupon coupon, CartDto cart);
}
