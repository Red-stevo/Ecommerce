package org.codiz.onshop.entities.orders;

import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.dtos.response.PaymentDetails;
import org.codiz.onshop.entities.users.Users;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Orders {

    @Id
   private String orderId;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "userId")
    private Users userId;
    private Instant createdOn;
    private double totalAmount;
    private String latitude;
    private String longitude;
    private PaymentStatus paymentStatus;
    private ShippingStatus shippingStatus;
    private OrderStatus orderStatus;
    @OneToMany(mappedBy = "orderId",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItems> orderItems;


    @PrePersist
    public void generateId() {
        if (this.orderId == null) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            this.orderId = uuid.substring(0, 6);
        }
        if (this.orderStatus == null) {
            this.orderStatus = OrderStatus.NOT_CANCELLED;
        }
        if (this.shippingStatus == null) {
            this.shippingStatus = ShippingStatus.UNDELIVERED;
        }
        if (this.getCreatedOn() == null) {
            this.createdOn = Instant.now();
        }
        if (this.getPaymentStatus() == null) {
            this.paymentStatus = PaymentStatus.NOT_PAID;
        }
    }

}
