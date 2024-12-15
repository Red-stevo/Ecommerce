package org.codiz.onshop.dtos.response;

import lombok.Data;

@Data
public class PaymentProducts {


    private String productId;
    private String productName;
    private float productPrice;
    private int productCount;
    private String productImage;

}


