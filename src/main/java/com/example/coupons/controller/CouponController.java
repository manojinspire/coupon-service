package com.example.coupons.controller;


import com.example.coupons.model.dtos.ApplicableCouponResponse;
import com.example.coupons.model.dtos.ApplyCouponResponse;
import com.example.coupons.model.dtos.CartDto;
import com.example.coupons.model.dtos.CouponDTO;
import com.example.coupons.model.entities.Coupon;
import com.example.coupons.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")

public class CouponController {


    private final CouponService couponService;
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/coupon")
    public ResponseEntity<Coupon> createCoupon(@RequestBody CouponDTO couponDTO) {
        Coupon coupon = couponService.createCoupon(couponDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(coupon);
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/coupon/{id}")
    public ResponseEntity<Coupon> getCoupon(@PathVariable Long id) {
        Coupon coupon = couponService.getCoupon(id);
        return ResponseEntity.ok(coupon);
    }

    @PutMapping("/coupon/{id}")
    public ResponseEntity<Coupon> updateCoupon(
            @PathVariable Long id,
            @RequestBody CouponDTO couponDTO) {
        Coupon coupon = couponService.updateCoupon(id, couponDTO);
        return ResponseEntity.ok(coupon);
    }

    @DeleteMapping("/coupon/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/applicable-coupons")
    public ResponseEntity<List<ApplicableCouponResponse>> getApplicableCoupons(
            @RequestBody CartDto cart) {
        List<ApplicableCouponResponse> applicableCoupons =
                couponService.getApplicableCoupons(cart);
        return ResponseEntity.ok(applicableCoupons);
    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<ApplyCouponResponse> applyCoupon(
            @PathVariable Long id,
            @RequestBody CartDto cart) {
        ApplyCouponResponse response = couponService.applyCoupon(id, cart);
        return ResponseEntity.ok(response);
    }
}

