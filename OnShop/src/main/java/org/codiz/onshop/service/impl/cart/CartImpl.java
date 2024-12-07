package org.codiz.onshop.service.impl.cart;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.CartItemsToAdd;
import org.codiz.onshop.dtos.requests.CartItemsUpdate;
import org.codiz.onshop.dtos.response.CartItemsResponse;
import org.codiz.onshop.dtos.response.CartResponse;
import org.codiz.onshop.dtos.response.YouMayLike;
import org.codiz.onshop.entities.cart.Cart;
import org.codiz.onshop.entities.cart.CartItems;
import org.codiz.onshop.entities.products.Categories;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.entities.products.SpecificProductDetails;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.cart.CartItemsRepository;
import org.codiz.onshop.repositories.cart.CartRepository;
import org.codiz.onshop.repositories.products.ProductsJpaRepository;
import org.codiz.onshop.repositories.products.SpecificProductsRepository;
import org.codiz.onshop.repositories.users.UserProfilesRepository;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.serv.cart.CartService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductsJpaRepository productsRepository;
    private final CartItemsRepository cartItemsRepository;
    private final UsersRepository usersRepository;
    private final SpecificProductsRepository specificProductsRepository;
    private final UserProfilesRepository profilesRepository;



    @Transactional
    public Cart addItemToCart(CartItemsToAdd items) {
        Users users = usersRepository.findUsersByUserId(items.getUserId());


        if (users == null) {
            throw new IllegalArgumentException("User not found");
        }

        Cart cart = cartRepository.findCartByUsers(users)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUsers(users);
                    return cartRepository.save(newCart);
                });

       /* Products product = productsRepository.findById(items.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));*/

        SpecificProductDetails productDetails = specificProductsRepository.findBySpecificProductId(items.getSpecificationId());



        CartItems existingCartItem = cartItemsRepository.findByCart(cart);
        System.out.println(existingCartItem);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + items.getQuantity());
            cartItemsRepository.save(existingCartItem);
        } else {
            CartItems newCartItem = new CartItems();
            newCartItem.setProducts(productDetails);
            newCartItem.setQuantity(items.getQuantity());
            newCartItem.setCart(cart);
            cartItemsRepository.save(newCartItem);
            cart.addCartItem(newCartItem);
        }


        return cartRepository.save(cart);
    }


    @Transactional
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



    @Transactional
    public CartResponse getCartById(String userId, Pageable pageable) {
        Users usr = usersRepository.findUsersByUserId(userId);
        Optional<Cart> cart = cartRepository.findCartByUsers(usr);

        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Cart not found");
        }

        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartId(cart.get().getCartId());
        cartResponse.setUsername(cart.get().getUsers().getUsername());

        // Map cart items to response
        List<CartItemsResponse> itemsResponses = new ArrayList<>();
        for (CartItems items : cart.get().getCartItems()) {
            CartItemsResponse itemsResponse = new CartItemsResponse();
            Products products = productsRepository.findByProductId(items.getProducts().getSpecificProductId());

            itemsResponse.setCartItemId(items.getCartItemId());
            itemsResponse.setCount(items.getQuantity());
            itemsResponse.setProductId(products.getProductId());
            itemsResponse.setProductName(products.getProductName());
            itemsResponse.setProductImageUrl(items.getProducts().getProductImagesList().get(0).getImageUrl());
            itemsResponse.setProductPrice(items.getProducts().getProductPrice()-items.getProducts().getDiscount());
            SpecificProductDetails details = specificProductsRepository.findBySpecificProductId(items.getProducts().getSpecificProductId());
            itemsResponse.setInStock(details.getCount() > 0);

            itemsResponses.add(itemsResponse);
        }
        cartResponse.setCartItemsResponses(itemsResponses);

        // Fetch "You May Like" products
        List<String> categoryIds = cart.get().getCartItems().stream()
                .flatMap(item -> item.getProducts().getProducts().getCategoriesList().stream().map(Categories::getCategoryId))
                .distinct()
                .toList();

        Page<Products> productsPage = productsRepository.findAllByCategoriesList_CategoryIdIn(categoryIds, pageable);

        List<YouMayLike> youMayLikes = productsPage.stream().map(product -> {
            YouMayLike like = new YouMayLike();
            like.setProductId(product.getProductId());
            like.setProductName(product.getProductName());
            like.setProductPrice(product.getSpecificProductDetailsList().get(0).getProductPrice());
            like.setProductImageUrl(product.getSpecificProductDetailsList()
                    .get(0).getProductImagesList().isEmpty() ? null :
                    product.getSpecificProductDetailsList().get(0).getProductImagesList().get(0).getImageUrl());
            return like;
        }).toList();



        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), youMayLikes.size());
        List<YouMayLike> pagedYouMayLikes = youMayLikes.subList(start, end);

        Page<YouMayLike> youMayLikePage = new PageImpl<>(pagedYouMayLikes, pageable, youMayLikes.size());

        // Add Pagination Metadata
        cartResponse.setYouMayLikes(youMayLikePage.getContent());
        cartResponse.setCurrentPage(youMayLikePage.getNumber());
        cartResponse.setTotalPages(youMayLikePage.getTotalPages());
        cartResponse.setHasMore(youMayLikePage.hasNext());

        return cartResponse;
    }

    @Transactional
    public HttpStatus removeItemFromCart(String cartItemId) {
        CartItems cartItems = cartItemsRepository.findCartItemsByCartItemId(cartItemId);
        cartItemsRepository.delete(cartItems);

        return HttpStatus.OK ;
    }

    @Transactional
    public HttpStatus deleteCart(String cartId){
        Cart cart = cartRepository.findCartByCartId(cartId);
        cartRepository.delete(cart);
        return HttpStatus.OK ;
    }


}
