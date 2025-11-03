package com.example.coupons.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bxgy_coupons")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BXGYCoupon extends Coupon {

    private Integer buyQuantity;

    private Integer getQuantity;

    private Integer repetitionLimit;

    @Column(nullable = false)
    private Boolean mixAndMatch = false;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BuyItem> buyItems = new ArrayList<>();

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<GetItem> getItems = new ArrayList<>();
}
