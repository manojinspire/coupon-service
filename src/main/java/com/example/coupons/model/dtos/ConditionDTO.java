package com.example.coupons.model.dtos;

import lombok.Data;

@Data
public class ConditionDTO {
    private String type;
    private String logicalOperator;
    private String conditionData;
    private String errorMessage;
}
