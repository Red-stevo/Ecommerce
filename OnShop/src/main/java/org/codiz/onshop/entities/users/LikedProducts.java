package org.codiz.onshop.entities.users;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class LikedProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String category;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserProfiles userProfiles;
}
