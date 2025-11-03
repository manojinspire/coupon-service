package com.example.coupons.service;



import com.example.coupons.enums.CouponsType;
import com.example.coupons.enums.DiscountScope;
import com.example.coupons.enums.DiscountType;
import com.example.coupons.exceptions.CouponNotFoundException;
import com.example.coupons.exceptions.InvalidCouponException;
import com.example.coupons.model.dtos.*;
import com.example.coupons.model.entities.CashbackCoupon;
import com.example.coupons.model.entities.Coupon;
import com.example.coupons.model.entities.CouponCondition;
import com.example.coupons.model.entities.DiscountCoupon;
import com.example.coupons.repositories.CouponRepository;
import com.example.coupons.service.strategy.CashbackCouponStrategy;
import com.example.coupons.service.strategy.CouponStrategy;
import com.example.coupons.service.strategy.DiscountCouponStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {
    private final CouponRepository couponRepository;
    private final ConditonsEvaluator conditionEvaluator;
    private final DiscountCouponStrategy discountStrategy;

    private final CashbackCouponStrategy cashbackStrategy;


    public Coupon createCoupon(CouponDTO couponDTO) {
        // Check if code already exists
        if (couponRepository.existsByCode(couponDTO.getCode())) {
            throw new InvalidCouponException("Coupon code already exists");
        }

        Coupon coupon = createCouponFromDTO(couponDTO);
        return couponRepository.save(coupon);
    }

    public Coupon getCoupon(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon updateCoupon(Long id, CouponDTO couponDTO) {
        Coupon existingCoupon = getCoupon(id);

        // Update common fields
        existingCoupon.setDescription(couponDTO.getDescription());
        existingCoupon.setExpiryDate(couponDTO.getExpiryDate());
        existingCoupon.setIsActive(couponDTO.getIsActive());
        existingCoupon.setUsageLimit(couponDTO.getUsageLimit());
        existingCoupon.setUpdatedAt(LocalDateTime.now());

        return couponRepository.save(existingCoupon);
    }

    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new CouponNotFoundException("Coupon not found with id: " + id);
        }
        couponRepository.deleteById(id);
    }

    public List<ApplicableCouponResponse> getApplicableCoupons(CartDto cart) {
        List<Coupon> activeCoupons = couponRepository.findByIsActiveTrue();
        List<ApplicableCouponResponse> applicableCoupons = new ArrayList<>();

        for (Coupon coupon : activeCoupons) {
            if (!coupon.canBeUsed()) {
                continue;
            }

            // Evaluate conditions
            if (!conditionEvaluator.evaluateConditions(coupon.getConditions(), cart)) {
                continue;
            }

            // Get appropriate strategy and check if applicable
            CouponStrategy strategy = getStrategy(coupon.getType());
            if (!strategy.isApplicable(coupon, cart)) {
                continue;
            }

            // Calculate discount
            DiscountResult result = strategy.apply(coupon, cart);

            ApplicableCouponResponse response = ApplicableCouponResponse.builder()
                    .couponId(coupon.getId())
                    .couponCode(coupon.getCode())
                    .type(coupon.getType().name())
                    .discountAmount(result.getDiscountAmount())
                    .cashbackAmount(result.getCashbackAmount())
                    .isApplicable(true)
                    .message(result.getMessage())
                    .build();

            applicableCoupons.add(response);
        }

        // Sort by discount amount (highest first)
        applicableCoupons.sort(Comparator.comparing(
                ApplicableCouponResponse::getDiscountAmount,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        return applicableCoupons;
    }

    public ApplyCouponResponse applyCoupon(Long couponId, CartDto cart) {
        Coupon coupon = getCoupon(couponId);

        // Validate coupon
        if (!coupon.canBeUsed()) {
            throw new InvalidCouponException("Coupon cannot be used (expired or usage limit reached)");
        }

        // Evaluate conditions
        if (!conditionEvaluator.evaluateConditions(coupon.getConditions(), cart)) {
            throw new InvalidCouponException("Cart does not meet coupon conditions");
        }

        // Get strategy and apply
        CouponStrategy strategy = getStrategy(coupon.getType());

        if (!strategy.isApplicable(coupon, cart)) {
            throw new InvalidCouponException("Coupon is not applicable to this cart");
        }

        DiscountResult result = strategy.apply(coupon, cart);

        if (!result.getSuccess()) {
            throw new InvalidCouponException(result.getMessage());
        }

        // Update cart with discounts
        CartDto updatedCart = applyDiscountsToCart(cart, result);

        // Increment usage count
        coupon.incrementUsage();
        couponRepository.save(coupon);

        return ApplyCouponResponse.builder()
                .updatedCart(updatedCart)
                .totalDiscount(result.getDiscountAmount())
                .cashbackAmount(result.getCashbackAmount())
                .message(result.getMessage())
                .success(true)
                .build();
    }

    private CouponStrategy getStrategy(CouponsType type) {
        return switch (type) {
            case DISCOUNT -> discountStrategy;

            case BXGY -> null;
            case CASHBACK -> cashbackStrategy;
//            case TRIP_DISCOUNT -> tripDiscountStrategy;
            case TRIP_DISCOUNT -> null;
            case VOCHER -> null;
        };
    }

    private CartDto applyDiscountsToCart(CartDto cart, DiscountResult result) {
        // Clone the cart to avoid modifying the original
        CartDto updatedCart = new CartDto();
        updatedCart.setUserId(cart.getUserId());
        updatedCart.setUserType(cart.getUserType());
        updatedCart.setPaymentMethod(cart.getPaymentMethod());
        updatedCart.setLocation(cart.getLocation());
        updatedCart.setTripDetails(cart.getTripDetails());

        // Copy items
        List<CartItemDTO> updatedItems = new ArrayList<>();
        for (CartItemDTO item : cart.getItems()) {
            CartItemDTO updatedItem = new CartItemDTO();
            updatedItem.setProductId(item.getProductId());
            updatedItem.setProductName(item.getProductName());
            updatedItem.setCategoryId(item.getCategoryId());
            updatedItem.setPrice(item.getPrice());
            updatedItem.setQuantity(item.getQuantity());
            updatedItem.setDiscount(item.getDiscount());

            // Mark free items
            if (result.getFreeItemIds() != null &&
                    result.getFreeItemIds().contains(item.getProductId())) {
                updatedItem.setIsFree(true);
            }

            updatedItems.add(updatedItem);
        }

        updatedCart.setItems(updatedItems);
        return updatedCart;
    }

    private Coupon createCouponFromDTO(CouponDTO dto) {
        return switch (dto.getType()) {
            case DISCOUNT -> createDiscountCoupon(dto);
            case BXGY -> null;
            case CASHBACK -> createCashbackCoupon(dto);

            case TRIP_DISCOUNT -> null;
            case VOCHER -> null;
        };
    }

    private DiscountCoupon createDiscountCoupon(CouponDTO dto) {
        DiscountCoupon coupon = new DiscountCoupon();
        setCommonFields(coupon, dto);

        Map<String, Object> details = dto.getCouponDetails();
        coupon.setDiscountType(
                DiscountType.valueOf((String) details.get("discountType"))
        );
        coupon.setDiscountValue(((Number) details.get("discountValue")).doubleValue());
        coupon.setMaxDiscountAmount(
                details.get("maxDiscountAmount") != null ?
                        ((Number) details.get("maxDiscountAmount")).doubleValue() : null
        );
        coupon.setDiscountScope(
                DiscountScope.valueOf((String) details.get("discountScope"))
        );
        coupon.setApplicableProductIds((String) details.get("applicableProductIds"));
        coupon.setApplicableCategoryIds((String) details.get("applicableCategoryIds"));

        return coupon;
    }



    private CashbackCoupon createCashbackCoupon(CouponDTO dto) {
        CashbackCoupon coupon = new CashbackCoupon();
        setCommonFields(coupon, dto);

        Map<String, Object> details = dto.getCouponDetails();
        coupon.setCashbackType(
                com.example.coupons.enums.CashbackType.valueOf((String) details.get("cashbackType"))
        );
        coupon.setCashbackValue(((Number) details.get("cashbackValue")).doubleValue());
        coupon.setMaxCashback(
                details.get("maxCashback") != null ?
                        ((Number) details.get("maxCashback")).doubleValue() : null
        );
        coupon.setMinPurchaseAmount(
                details.get("minPurchaseAmount") != null ?
                        ((Number) details.get("minPurchaseAmount")).doubleValue() : null
        );
        coupon.setWalletId((String) details.get("walletId"));

        return coupon;
    }


    private void setCommonFields(Coupon coupon, CouponDTO dto) {
        coupon.setCode(dto.getCode());
        coupon.setType(dto.getType());
        coupon.setDescription(dto.getDescription());
        coupon.setExpiryDate(dto.getExpiryDate());
        coupon.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        coupon.setUsageLimit(dto.getUsageLimit());
        coupon.setCreatedAt(LocalDateTime.now());

        // Add conditions
        if (dto.getConditions() != null) {
            for (ConditionDTO conditionDTO : dto.getConditions()) {
                CouponCondition condition = new CouponCondition();
                condition.setType(
                        com.example.coupons.enums.ConditionType.valueOf(conditionDTO.getType())
                );
                condition.setConditionData(conditionDTO.getConditionData());
                condition.setLogicalOperator(
                        conditionDTO.getLogicalOperator() != null ?
                                com.example.coupons.enums.LogicalOperator.valueOf(conditionDTO.getLogicalOperator()) :
                                null
                );
                condition.setErrorMessage(conditionDTO.getErrorMessage());
                coupon.addCondition(condition);
            }
        }
    }
}
