package org.codiz.onshop.entities.messaging;

import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.entities.users.Users;

@Entity
@Data
public class InAppNotifications {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String notificationId;
    private String message;
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private Users users;
}
