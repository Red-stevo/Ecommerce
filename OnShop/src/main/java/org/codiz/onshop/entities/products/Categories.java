package org.codiz.onshop.entities.products;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String categoryId;
    private String categoryName;
    private String categoryIcon;

    @ManyToMany(mappedBy = "categoriesList", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    @JsonIgnore
    @ToString.Exclude
    private List<Products> products;

}
