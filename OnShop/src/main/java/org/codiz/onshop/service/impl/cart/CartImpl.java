package org.codiz.onshop.service.impl.cart;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.CartItemsDeletion;
import org.codiz.onshop.dtos.requests.CartItemsToAdd;
import org.codiz.onshop.dtos.requests.CartItemsUpdate;
import org.codiz.onshop.entities.cart.Cart;
import org.codiz.onshop.entities.cart.CartItems;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.cart.CartItemsRepository;
import org.codiz.onshop.repositories.cart.CartRepository;
import org.codiz.onshop.repositories.products.ProductsJpaRepository;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.serv.cart.CartService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductsJpaRepository productsRepository;
    private final CartItemsRepository cartItemsRepository;
    private final UsersRepository usersRepository;



    public Cart addItemToCart(CartItemsToAdd items) {
        Users users = usersRepository.findUsersByUserId(items.getUserId());
        Cart cart = cartRepository.findCartByUserId(items.getUserId())
                .orElseGet(()->{
                    Cart newCart = new Cart();
                    newCart.setUsers(users);
                    return cartRepository.save(newCart);
                });

        Products product = productsRepository.findById(items.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Check if item already exists in the cart
        Optional<CartItems> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProducts().getProductId().equals(items.getProductId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItems cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + items.getQuantity());
        } else {
            CartItems newCartItem = new CartItems();
            newCartItem.setProducts(product);
            newCartItem.setQuantity(items.getQuantity());
            cart.addCartItem(newCartItem);
        }

        return cartRepository.save(cart);
    }

    public Cart updateItemQuantity(CartItemsUpdate itemsUpdate) {
        Cart cart = cartRepository.findById(itemsUpdate.getCartId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        CartItems cartItem = cartItemsRepository.findById(itemsUpdate.getCartItemId())
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        if (itemsUpdate.getQuantity() > 0) {
            cartItem.setQuantity(itemsUpdate.getQuantity());
        } else {
            cart.removeCartItem(cartItem);
            cartItemsRepository.delete(cartItem);
        }

        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(CartItemsDeletion deletion) {
        Cart cart = cartRepository.findById(deletion.getCartId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        CartItems cartItem = cartItemsRepository.findById(deletion.getCartItemId())
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        cart.removeCartItem(cartItem);
        cartItemsRepository.delete(cartItem);

        return cartRepository.save(cart);
    }

    public Optional<Cart> getCartById(String cartId) {
        return cartRepository.findById(cartId);
    }



}
