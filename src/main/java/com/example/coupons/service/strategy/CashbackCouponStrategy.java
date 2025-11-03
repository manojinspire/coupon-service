package com.example.coupons.service.strategy;

import com.example.coupons.enums.CashbackType;
import com.example.coupons.model.dtos.CartDto;
import com.example.coupons.model.dtos.DiscountResult;
import com.example.coupons.model.entities.CashbackCoupon;
import com.example.coupons.model.entities.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CashbackCouponStrategy implements CouponStrategy {
    @Override
    public DiscountResult apply(Coupon coupon, CartDto cart) {
        CashbackCoupon cashbackCoupon = (CashbackCoupon) coupon;

        double cashback = calculateCashback(cashbackCoupon, cart);

        DiscountResult result = DiscountResult.success("Cashback will be credited to your wallet");
        result.setCouponCode(coupon.getCode());
        result.setCashbackAmount(cashback);
        result.setDiscountAmount(0.0); // Cashback doesn't reduce cart value immediately

        return result;
    }

    @Override
    public boolean isApplicable(Coupon coupon, CartDto cart) {
        CashbackCoupon cashbackCoupon = (CashbackCoupon) coupon;

        if (cashbackCoupon.getMinPurchaseAmount() != null) {
            return cart.calculateSubtotal() >= cashbackCoupon.getMinPurchaseAmount();
        }

        return true;
    }
    private double calculateCashback(CashbackCoupon coupon, CartDto cart) {
        double subtotal = cart.calculateSubtotal();
        double cashback = 0.0;

        if (coupon.getCashbackType() == CashbackType.PERCENTAGE) {
            cashback = subtotal * (coupon.getCashbackValue() / 100.0);
        } else if (coupon.getCashbackType() == CashbackType.FIXED_AMOUNT) {
            cashback = coupon.getCashbackValue();
        }

        if (coupon.getMaxCashback() != null) {
            cashback = Math.min(cashback, coupon.getMaxCashback());
        }

        return cashback;
    }
}
