package com.example.coupons.model.dtos;

import lombok.Data;

@Data
public class LocationDTO {
    private String city;
    private String state;
    private String pincode;
    private Double latitude;
    private Double longitude;
}
