package com.example.coupons.repositories;


import com.example.coupons.model.entities.CouponCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponConditionRepository extends JpaRepository<CouponCondition, Long> {
}