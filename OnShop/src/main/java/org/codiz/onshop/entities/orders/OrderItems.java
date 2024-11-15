package org.codiz.onshop.entities.orders;

import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.entities.products.Products;

@Entity
@Data
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderItemId;
    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "orderId")
    private Orders orderId;
    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "productId")
    private Products productId;
    private int quantity;
    private double totalPrice;
}
