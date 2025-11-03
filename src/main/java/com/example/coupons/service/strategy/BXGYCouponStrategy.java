package com.example.coupons.service.strategy;

import com.example.coupons.model.dtos.CartDto;
import com.example.coupons.model.dtos.DiscountResult;
import com.example.coupons.model.entities.Coupon;
import org.springframework.stereotype.Component;

@Component
public class BXGYCouponStrategy implements CouponStrategy {
    @Override
    public DiscountResult apply(Coupon coupon, CartDto cart) {
        return null;
    }

    @Override
    public boolean isApplicable(Coupon coupon, CartDto cart) {
        return false;
    }
}
