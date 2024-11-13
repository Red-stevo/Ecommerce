package org.codiz.onshop.service.impl.cart;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.CartItemsToAdd;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.entities.cart.Cart;
import org.codiz.onshop.entities.cart.CartItems;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.repositories.cart.CartItemsRepository;
import org.codiz.onshop.repositories.cart.CartRepository;
import org.codiz.onshop.repositories.products.ProductsJpaRepository;
import org.codiz.onshop.service.serv.cart.CartsItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class CartItemsImpl implements CartsItemsService {
    private final CartItemsRepository cartItemsRepository;
    private final CartRepository cartRepository;
    private final ProductsJpaRepository productsRepository;


    public EntityResponse addItemToCart(CartItemsToAdd cartItemsToAdd) {
        Cart cart = cartRepository.findCartByCartId(cartItemsToAdd.getCartId());
        Products product = productsRepository.findByProductId(cartItemsToAdd.getProductId());

        CartItems items = new CartItems();
        items.setCart(cart);
        items.setProducts(product);
        items.setQuantity(cartItemsToAdd.getQuantity());

        cartItemsRepository.save(items);

        EntityResponse entityCreationResponse = new EntityResponse();
        entityCreationResponse.setMessage("Successfully added item to the cart");
        entityCreationResponse.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entityCreationResponse.setStatus(HttpStatus.OK);
        return entityCreationResponse;
    }

    public EntityResponse removeItemFromCart(Products products) {

        CartItems items = cartItemsRepository.findCartItemsByProducts(products);
        if (items != null) {
            cartItemsRepository.delete(items);
            EntityResponse entityCreationResponse = new EntityResponse();
            entityCreationResponse.setMessage("Successfully removed item from the cart");
            entityCreationResponse.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            entityCreationResponse.setStatus(HttpStatus.OK);
            return entityCreationResponse;
        }else {
            EntityResponse entityCreationResponse = new EntityResponse();
            entityCreationResponse.setMessage("No item found to remove from the cart");
            entityCreationResponse.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            entityCreationResponse.setStatus(HttpStatus.NOT_FOUND);
            return entityCreationResponse;
        }
    }

    public EntityResponse updateCartItem(Integer quantity,String cartItemId) {
        CartItems cartItems = cartItemsRepository.findCartItemsByCartItemId(cartItemId);
        if (cartItems != null) {
            cartItems.setQuantity(quantity);
            cartItemsRepository.save(cartItems);
            EntityResponse entityCreationResponse = new EntityResponse();
            entityCreationResponse.setMessage("Successfully updated quantity");
            entityCreationResponse.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            entityCreationResponse.setStatus(HttpStatus.OK);
            return entityCreationResponse;
        }else {
            EntityResponse entityCreationResponse = new EntityResponse();
            entityCreationResponse.setMessage("No item found to update quantity");
            entityCreationResponse.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            entityCreationResponse.setStatus(HttpStatus.NOT_FOUND);
            return entityCreationResponse;
        }
    }

}
