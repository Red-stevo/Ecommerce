package org.codiz.onshop.repositories.order;

import org.codiz.onshop.entities.orders.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersItemsRepository extends JpaRepository<OrderItems,String> {
    Optional<OrderItems> findOrderItemsByOrderItemId(String orderItemId);
}
