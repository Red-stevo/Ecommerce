package org.codiz.onshop.entities.messaging;

import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.entities.users.Users;

import java.time.LocalDateTime;

@Entity
@Data
public class InAppNotifications {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String notificationId;

        private String message;
        private Boolean isRead = false; // Default to unread

        @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinColumn(name = "userId")
        private Users users;

        private LocalDateTime createdAt; // Timestamp for when the notification was created

        @PrePersist
        protected void onCreate() {
            this.createdAt = LocalDateTime.now();
        }

       
}
