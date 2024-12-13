package org.codiz.onshop.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.entities.users.Users;

import java.util.List;

@Entity
@Data
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String wishlistId;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Users user;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "wishlist_products")
    @JsonIgnore
    private List<SpecificProductDetails> specificProductDetails;

}
