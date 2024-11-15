package org.codiz.onshop.repositories.order;

import org.codiz.onshop.entities.orders.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, String> {
}
