package org.codiz.onshop.entities.orders;

import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.entities.products.SpecificProductDetails;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderItemId;
    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "orderId")
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Orders orderId;
    @ManyToOne
    @JoinColumn(name = "specific_product_id",referencedColumnName = "specificProductId")
    private SpecificProductDetails specificProductDetails;

    private int quantity;
    private double totalPrice;
    private OrderItemStatus status;

    @PrePersist
    public void Status() {
        if (status == null) {
            this.status = OrderItemStatus.PENDING;
        }
    }
}
