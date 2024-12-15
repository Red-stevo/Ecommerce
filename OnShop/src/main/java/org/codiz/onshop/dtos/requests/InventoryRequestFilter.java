package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.codiz.onshop.entities.products.InventoryStatus;

@Data
public class InventoryRequestFilter {

    InventoryStatus inventoryStatus;
    String categoryName;
    Float price1;
    Float price2;
}
