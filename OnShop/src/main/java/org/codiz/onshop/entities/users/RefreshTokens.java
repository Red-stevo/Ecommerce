package org.codiz.onshop.entities.users;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RefreshTokens {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String tokenId;

    private String refreshToken;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private Users user;

}
