package org.codiz.onshop.repositories.messaging;

import org.codiz.onshop.entities.messaging.InAppNotifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InAppNotificationsRepository extends JpaRepository<InAppNotifications,String> {

}
