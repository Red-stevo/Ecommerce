package org.codiz.onshop.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class PaymentDetails {

        private String username;
        private String orderId;
        private String phoneNumber;
        private String location;
        private List<PaymentProducts> products;

        private float productsAmount;
        private float shippingCost;

}
