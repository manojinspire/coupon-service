package com.example.coupons.service.strategy;

import com.example.coupons.enums.DiscountScope;
import com.example.coupons.enums.DiscountType;
import com.example.coupons.model.dtos.CartDto;
import com.example.coupons.model.dtos.CartItemDTO;
import com.example.coupons.model.dtos.DiscountResult;
import com.example.coupons.model.entities.Coupon;
import com.example.coupons.model.entities.DiscountCoupon;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DiscountCouponStrategy implements CouponStrategy {

    @Override
    public DiscountResult apply(Coupon coupon, CartDto cart) {
        DiscountCoupon discountCoupon = (DiscountCoupon) coupon;

        DiscountResult result = DiscountResult.success("Discount applied successfully");
        result.setCouponCode(coupon.getCode());

        if (discountCoupon.getDiscountScope() == DiscountScope.CART_LEVEL) {
            double discount = calculateCartDiscount(discountCoupon, cart);
            result.setDiscountAmount(discount);
        } else if (discountCoupon.getDiscountScope() == DiscountScope.PRODUCT_LEVEL) {
            double totalDiscount = calculateProductDiscount(discountCoupon, cart);
            result.setDiscountAmount(totalDiscount);
        }

        return result;
    }

    @Override
    public boolean isApplicable(Coupon coupon, CartDto cart) {
        DiscountCoupon discountCoupon = (DiscountCoupon) coupon;

        if (discountCoupon.getDiscountScope() == DiscountScope.PRODUCT_LEVEL) {
            String productIds = discountCoupon.getApplicableProductIds();
            if (productIds != null && !productIds.isEmpty()) {
                List<String> applicableProducts = Arrays.asList(productIds.split(","));
                return cart.getItems().stream()
                        .anyMatch(item -> applicableProducts.contains(item.getProductId()));
            }
        }

        return true;
    }

    private double calculateCartDiscount(DiscountCoupon coupon, CartDto cart) {
        double subtotal = cart.calculateSubtotal();
        double discount = 0.0;

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            discount = subtotal * (coupon.getDiscountValue() / 100.0);
        } else if (coupon.getDiscountType() == DiscountType.FIXED_AMOUNT) {
            discount = coupon.getDiscountValue();
        }
        if (coupon.getMaxDiscountAmount() != null) {
            discount = Math.min(discount, coupon.getMaxDiscountAmount());
        }

        return Math.min(discount, subtotal);
    }

    private double calculateProductDiscount(DiscountCoupon coupon, CartDto cart) {
        String productIds = coupon.getApplicableProductIds();
        if (productIds == null || productIds.isEmpty()) {
            return 0.0;
        }

        List<String> applicableProducts = Arrays.asList(productIds.split(","));
        double totalDiscount = 0.0;

        for (CartItemDTO item : cart.getItems()) {
            if (applicableProducts.contains(item.getProductId())) {
                double itemTotal = item.getPrice() * item.getQuantity();
                double itemDiscount = 0.0;

                if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
                    itemDiscount = itemTotal * (coupon.getDiscountValue() / 100.0);
                } else {
                    itemDiscount = coupon.getDiscountValue() * item.getQuantity();
                }

                item.setDiscount(itemDiscount);
                totalDiscount += itemDiscount;
            }
        }

        if (coupon.getMaxDiscountAmount() != null) {
            totalDiscount = Math.min(totalDiscount, coupon.getMaxDiscountAmount());
        }

        return totalDiscount;
    }
}
