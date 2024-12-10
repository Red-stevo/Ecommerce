package org.codiz.onshop.entities.messaging;

import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.entities.users.Users;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class InAppNotifications {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String notificationId;
    private String message;
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private Users users;
}
