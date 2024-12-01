package org.codiz.onshop.entities.users;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Users {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;
    private String username;
    private String phoneNumber;
    private String userEmail;
    private String password;
    private Role role;

    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private UserProfiles profile;

    @PrePersist
    protected void onCreate() {
        profile = new UserProfiles();
        profile.setUserId(this);
        profile.setGender(Gender.NOT_SPECIFIED);
        profile.setFullName("");
        profile.setAddress("");
        profile.setSecondaryEmail("");
        profile.setImageUrl("");
    }


}
