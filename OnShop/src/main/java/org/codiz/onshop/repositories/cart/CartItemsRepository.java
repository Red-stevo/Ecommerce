package org.codiz.onshop.repositories.cart;

import org.codiz.onshop.entities.cart.Cart;
import org.codiz.onshop.entities.cart.CartItems;
import org.codiz.onshop.entities.products.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, String> {
    CartItems findCartItemsByProducts(Products products);

    CartItems findCartItemsByCartItemId(String cartItemId);

    List<Object> findAllByCart(Cart cartId);

    CartItems findByCartAndProducts(Cart cart, Products product);
}
