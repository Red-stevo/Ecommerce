package org.codiz.onshop.repositories.order;

import org.codiz.onshop.entities.orders.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersItemsRepository extends JpaRepository<OrderItems,String> {
}
