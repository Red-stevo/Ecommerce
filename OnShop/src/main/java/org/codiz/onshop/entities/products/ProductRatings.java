package org.codiz.onshop.entities.products;

import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.entities.users.Users;


@Entity
@Data
public class ProductRatings {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String ratingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Products product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    private int rating;

    private String review;
}
