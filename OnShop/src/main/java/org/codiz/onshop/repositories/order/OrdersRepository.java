package org.codiz.onshop.repositories.order;

import org.codiz.onshop.entities.orders.Orders;
import org.codiz.onshop.entities.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, String> {
    List<Orders> findAllByUserIdOrderByCreatedOnDesc(Users userId);


}