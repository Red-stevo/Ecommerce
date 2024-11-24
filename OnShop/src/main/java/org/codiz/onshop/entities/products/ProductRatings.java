package org.codiz.onshop.entities.products;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.codiz.onshop.entities.users.Users;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Data
public class ProductRatings {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String ratingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    //@OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Products product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    private int rating;

    private String review;
}
