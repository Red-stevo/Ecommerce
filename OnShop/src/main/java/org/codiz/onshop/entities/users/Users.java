package org.codiz.onshop.entities.users;

import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.entities.messaging.InAppNotifications;
import org.codiz.onshop.entities.orders.Orders;
import org.codiz.onshop.entities.products.WishList;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Users implements UserDetails {


    @Id
    private String userId;
    private String username;
    private String phoneNumber;
    private String userEmail;
    private String password;
    private Role role;

    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private UserProfiles profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private WishList wishList;

    @OneToMany(mappedBy = "orderId", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Orders> orders;

    /*@OneToMany(mappedBy = "users", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<InAppNotifications> notificationsList = new ArrayList<>();*/

    @PrePersist
    protected void onCreate() {
        profile = new UserProfiles();
        profile.setUserId(this);
        profile.setGender(Gender.NOT_SPECIFIED);
        profile.setFullName("");
        profile.setAddress("");
        profile.setSecondaryEmail("");
        profile.setImageUrl("");

        if (this.userId == null) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            this.userId = uuid.substring(0, 4);
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
