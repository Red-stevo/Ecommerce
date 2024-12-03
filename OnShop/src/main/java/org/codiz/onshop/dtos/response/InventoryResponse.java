package org.codiz.onshop.dtos.response;

import lombok.Data;

import java.time.Instant;

@Data
public class InventoryResponse {
    private String productName;
    private int inStockQuantity;
    private int quantitySold;
    private double buyingPrice;
    private double totalCost;
    private double sellingPrice;
    private double profit;
    private String lastUpdate;
}
