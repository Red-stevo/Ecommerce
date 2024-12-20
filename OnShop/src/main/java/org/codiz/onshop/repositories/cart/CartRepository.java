package org.codiz.onshop.repositories.cart;

import org.codiz.onshop.entities.cart.Cart;
import org.codiz.onshop.entities.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Cart findCartByCartId(String cartId);

    Optional<Cart> findCartByUsers(Users users);

    Optional<Object> findAllByCartId(String cartId);

    Optional<Object> findByCartId(String cartId);
}
