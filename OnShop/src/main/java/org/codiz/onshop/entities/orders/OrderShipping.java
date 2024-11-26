package org.codiz.onshop.entities.orders;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Data
public class OrderShipping {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String shippingId;
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Orders order;
    private Instant shippingDate;
    private Instant deliveryDate;
    private String trackingId;
    private ShippingStatus shippingStatus;

}
