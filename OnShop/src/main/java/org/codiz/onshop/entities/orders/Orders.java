package org.codiz.onshop.entities.orders;

import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.entities.users.Users;

import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "userId")
    private Users userId;
    private Timestamp createdOn;
    private double totalAmount;
    private String officeAddress;
    private String doorStepAddress;
    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItems> orderItems;
}