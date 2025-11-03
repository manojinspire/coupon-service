package com.example.coupons.model.entities;

import com.example.coupons.enums.ConditionType;
import com.example.coupons.enums.LogicalOperator;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
// conditions that should be validated to apply coupon successfully.
@Entity
@Table(name = "coupon_conditions")
@Data
@NoArgsConstructor
public class CouponCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    @JsonBackReference
    private Coupon coupon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionType type;

    @Column(length = 2000)
    private String conditionData; // JSON string for flexible condition data

    @Enumerated(EnumType.STRING)
    private LogicalOperator logicalOperator;

    @ManyToOne
    @JoinColumn(name = "parent_condition_id")
    private CouponCondition parentCondition;

    private String errorMessage;
}
