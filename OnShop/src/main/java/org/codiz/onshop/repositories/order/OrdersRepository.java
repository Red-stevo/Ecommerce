package org.codiz.onshop.repositories.order;

import org.codiz.onshop.entities.orders.OrderStatus;
import org.codiz.onshop.entities.orders.Orders;
import org.codiz.onshop.entities.users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, String> {
    List<Orders> findAllByUserIdOrderByCreatedOnDesc(Users userId);


    Orders findByOrderId(String orderId);

    List<Orders> findAllByCreatedOnBetweenOrderByCreatedOnDesc(Instant oneWeekAgo, Instant now);

    Page<Orders> findAllByOrderStatusOrderByCreatedOnAsc(OrderStatus orderStatus, Pageable pageable);
}
