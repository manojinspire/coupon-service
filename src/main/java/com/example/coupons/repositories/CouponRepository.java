package com.example.coupons.repositories;


import com.example.coupons.enums.CouponsType;
import com.example.coupons.model.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);
    List<Coupon> findByIsActiveTrue();
    List<Coupon> findByType(CouponsType type);
    boolean existsByCode(String code);
}
