package org.codiz.onshop.repositories.order;


import org.codiz.onshop.entities.orders.OrderShipping;
import org.codiz.onshop.entities.orders.ShippingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersShippingRepository extends JpaRepository<OrderShipping,String> {
    OrderShipping findByTrackingId(String trackingId);

    List<OrderShipping> findAllByShippingStatusEquals(ShippingStatus shippingStatus);
}
