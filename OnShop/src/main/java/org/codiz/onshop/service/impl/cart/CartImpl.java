package org.codiz.onshop.service.impl.cart;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.CartCreationRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.entities.cart.Cart;
import org.codiz.onshop.entities.cart.CartItems;
import org.codiz.onshop.entities.cart.Status;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.cart.CartItemsRepository;
import org.codiz.onshop.repositories.cart.CartRepository;
import org.codiz.onshop.repositories.products.ProductsJpaRepository;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.serv.cart.CartService;
import org.codiz.onshop.service.serv.cart.CartsItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductsJpaRepository productsRepository;
    private final CartItemsRepository cartItemsRepository;

    public Cart createCart(Cart cart) {

        return cartRepository.save(cart);
    }

    public Cart addItemToCart(String cartId, String productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Check if item already exists in the cart
        Optional<CartItems> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProducts().getProductId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItems cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItems newCartItem = new CartItems();
            newCartItem.setProducts(product);
            newCartItem.setQuantity(quantity);
            cart.addCartItem(newCartItem);
        }

        return cartRepository.save(cart);
    }

    public Cart updateItemQuantity(String cartId, String cartItemId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        CartItems cartItem = cartItemsRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        if (quantity > 0) {
            cartItem.setQuantity(quantity);
        } else {
            cart.removeCartItem(cartItem);
            cartItemsRepository.delete(cartItem);
        }

        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(String cartId, String cartItemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        CartItems cartItem = cartItemsRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        cart.removeCartItem(cartItem);
        cartItemsRepository.delete(cartItem);

        return cartRepository.save(cart);
    }

    public Optional<Cart> getCartById(String cartId) {
        return cartRepository.findById(cartId);
    }



}
