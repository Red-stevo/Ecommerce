package org.codiz.onshop.entities.users;

import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.entities.products.Products;

import java.util.List;

@Data
@Entity
public class UserProfiles {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String profileId;
    @OneToOne(fetch = FetchType.EAGER,orphanRemoval = true)
    private Users userId;
    private String fullName;
    private String imageUrl;
    private String secondaryEmail;
    private Gender gender;
    private String address;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "preferred_product",
            joinColumns = @JoinColumn(name = "profileId"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Products> likedProducts;
}