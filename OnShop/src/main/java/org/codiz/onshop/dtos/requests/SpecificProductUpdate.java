package org.codiz.onshop.dtos.requests;

import lombok.Data;

import java.util.List;
@Data
public class SpecificProductUpdate {

    private String specificProductId;

    private float productPrice;

    private String color;

    private String size;

    private float discount;

    private int count;

}
