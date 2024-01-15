package com.example.javaspringlearn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationForm {
    private Long productId;
    private String action;
    private Integer quant;
}
