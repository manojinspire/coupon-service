package com.example.coupons.service;

import com.example.coupons.model.dtos.CartDto;
import com.example.coupons.model.entities.CouponCondition;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ConditonsEvaluator {

        private final ObjectMapper objectMapper = new ObjectMapper();

        public boolean evaluateConditions(List<CouponCondition> conditions, CartDto cart) {
            if (conditions == null || conditions.isEmpty()) {
                return true;
            }

            // Evaluate root conditions (those without parent)
            List<CouponCondition> rootConditions = conditions.stream()
                    .filter(c -> c.getParentCondition() == null)
                    .toList();

            for (CouponCondition condition : rootConditions) {
                if (!evaluateCondition(condition, cart)) {
                    return false;
                }
            }

            return true;
        }

        private boolean evaluateCondition(CouponCondition condition, CartDto cart) {
            try {
                Map<String, Object> data = objectMapper.readValue(
                        condition.getConditionData(),
                        new TypeReference<Map<String, Object>>() {}
                );

                boolean result = switch (condition.getType()) {
                    case MIN_CART_VALUE -> evaluateMinCartValue(data, cart);
                    case MAX_CART_VALUE -> evaluateMaxCartValue(data, cart);
                    case USER_TYPE -> evaluateUserType(data, cart);
                    case PAYMENT_METHOD -> evaluatePaymentMethod(data, cart);
                    case PRODUCT_QUANTITY -> evaluateProductQuantity(data, cart);
                    default -> true;
                };

                return result;
            } catch (Exception e) {
                return false;
            }
        }

        private boolean evaluateMinCartValue(Map<String, Object> data, CartDto cart) {
            Double minValue = ((Number) data.get("minValue")).doubleValue();
            return cart.calculateSubtotal() >= minValue;
        }

        private boolean evaluateMaxCartValue(Map<String, Object> data, CartDto cart) {
            Double maxValue = ((Number) data.get("maxValue")).doubleValue();
            return cart.calculateSubtotal() <= maxValue;
        }

        private boolean evaluateUserType(Map<String, Object> data, CartDto cart) {
            String requiredType = (String) data.get("userType");
            return cart.getUserType() != null &&
                    cart.getUserType().equalsIgnoreCase(requiredType);
        }

        private boolean evaluatePaymentMethod(Map<String, Object> data, CartDto cart) {
            String requiredMethod = (String) data.get("paymentMethod");
            return cart.getPaymentMethod() != null &&
                    cart.getPaymentMethod().equalsIgnoreCase(requiredMethod);
        }

        private boolean evaluateProductQuantity(Map<String, Object> data, CartDto cart) {
            String productId = (String) data.get("productId");
            Integer minQuantity = (Integer) data.get("minQuantity");

            int totalQuantity = cart.getItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .mapToInt(item -> item.getQuantity())
                    .sum();

            return totalQuantity >= minQuantity;
        }
    }

