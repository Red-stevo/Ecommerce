package org.codiz.onshop.service.impl.cart;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.CartItemsToAdd;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.codiz.onshop.entities.cart.Cart;
import org.codiz.onshop.entities.cart.CartItems;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.repositories.cart.CartItemsRepository;
import org.codiz.onshop.repositories.cart.CartRepository;
import org.codiz.onshop.repositories.products.ProductsRepository;
import org.codiz.onshop.service.serv.cart.CartsItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class CartItemsImpl implements CartsItemsService {
    private final CartItemsRepository cartItemsRepository;
    private final CartRepository cartRepository;
    private final ProductsRepository productsRepository;


    public EntityCreationResponse addItemToCart(CartItemsToAdd cartItemsToAdd) {
        Cart cart = cartRepository.findCartByCartId(cartItemsToAdd.getCartId());
        Products product = productsRepository.findByProductId(cartItemsToAdd.getProductId());

        CartItems items = new CartItems();
        items.setCart(cart);
        items.setProducts(product);
        items.setQuantity(cartItemsToAdd.getQuantity());

        cartItemsRepository.save(items);

        EntityCreationResponse entityCreationResponse = new EntityCreationResponse();
        entityCreationResponse.setMessage("Successfully added item to the cart");
        entityCreationResponse.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entityCreationResponse.setStatus(HttpStatus.OK);
        return entityCreationResponse;
    }

}
