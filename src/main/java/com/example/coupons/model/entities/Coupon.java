package com.example.coupons.model.entities;

import com.example.coupons.enums.CouponsType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coupons")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponsType type;

    private String description;

    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private Boolean isActive = true;

    private Integer usageLimit;

    @Column(nullable = false)
    private Integer usedCount = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<CouponCondition> conditions = new ArrayList<>();
    public boolean isExpired() {
        return expiryDate != null && LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean canBeUsed() {
        return isActive && !isExpired() &&
                (usageLimit == null || usedCount < usageLimit);
    }

    public void incrementUsage() {
        this.usedCount++;
    }

    public void addCondition(CouponCondition condition) {
        conditions.add(condition);
        condition.setCoupon(this);
    }
}
