package com.example.coupons.model.dtos;


import com.example.coupons.enums.CouponsType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class CouponDTO {
    private Long id;
    private String code;
    private CouponsType type;
    private String description;
    private LocalDateTime expiryDate;
    private Boolean isActive;
    private Integer usageLimit;
    private Integer usedCount;

    // For different coupon types
    private Map<String, Object> couponDetails;
    private List<ConditionDTO> conditions;
}
