package org.codiz.onshop.dtos.response;

import lombok.Data;

@Data
public class CustomerDetails {
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private int numberOfItemsOrdered;
    private String orderNumber;
}
