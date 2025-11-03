package com.example.coupons.model.dtos;

import lombok.Data;

@Data
public class TripDetailsDTO {
    private String tripType;
    private String route;
    private String vehicleType;
    private Integer distance;
    private String timeSlot;
    private Double baseAmount;
}

