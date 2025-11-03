package com.example.coupons.enums;

public enum ConditionType {
    MIN_CART_VALUE,
    MAX_CART_VALUE,
    USER_TYPE,
    PAYMENT_METHOD,
    PRODUCT_QUANTITY,
    CATEGORY,
    DATE_TIME,
    LOCATION,
    ORDER_COUNT, // like first order, second order. give a coupon if it is third order.
    COMPOSITE // composite condition.  apply coupon if (cart value > 500 and payment_method through upi ) or (payment through credit card)

}
