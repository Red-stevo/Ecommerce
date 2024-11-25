package org.codiz.onshop.repositories.cart;

import org.codiz.onshop.entities.cart.Cart;
import org.codiz.onshop.entities.cart.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, String> {

    CartItems findByCart(Cart cart);

    CartItems findCartItemsByCartItemId(String cartItemId);
}
