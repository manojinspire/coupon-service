package com.example.coupons.model.entities;


import com.example.coupons.enums.CashbackType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cashback_coupons")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CashbackCoupon extends Coupon {

    @Enumerated(EnumType.STRING)
    private CashbackType cashbackType;

    private Double cashbackValue;

    private Double maxCashback;

    private Double minPurchaseAmount;
    private String walletId;
}
