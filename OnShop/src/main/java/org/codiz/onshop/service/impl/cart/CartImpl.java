package org.codiz.onshop.service.impl.cart;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.ControllerAdvice.custom.EntityDeletionException;
import org.codiz.onshop.ControllerAdvice.custom.ResourceCreationFailedException;
import org.codiz.onshop.ControllerAdvice.custom.ResourceNotFoundException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductsJpaRepository productsRepository;
    private final CartItemsRepository cartItemsRepository;
    private final UsersRepository usersRepository;
    private final SpecificProductsRepository specificProductsRepository;
    private final UserProfilesRepository profilesRepository;



    @Transactional
    public ResponseEntity addItemToCart(List<CartItemsToAdd> items,String userId) {
       try {
           System.out.println(userId);
           Users users = usersRepository.findUsersByUserId(userId);


           if (users == null) {
               throw new IllegalArgumentException("User not found");
           }

           Cart cart = cartRepository.findCartByUsers(users)
                   .orElseGet(() -> {
                       Cart newCart = new Cart();
                       newCart.setUsers(users);
                       return cartRepository.save(newCart);
                   });
                List<CartItems> cartItemsList = new ArrayList<>();
                log.info("specific :"+items);
            for (CartItemsToAdd item : items) {
                SpecificProductDetails productDetails = specificProductsRepository.findBySpecificProductId(item.getSpecificProductId())
                        .orElseThrow(()->new ResourceNotFoundException("product not found"));



                if (cartItemsRepository.existsByProducts(productDetails)){
                    log.info("item exists");
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                CartItems newCartItem = new CartItems();
                newCartItem.setProducts(productDetails);
                newCartItem.setQuantity(item.getQuantity());
                newCartItem.setCart(cart);
                cartItemsList.add(newCartItem);

            }

            cartItemsRepository.saveAll(cartItemsList);

           return new ResponseEntity<>(HttpStatus.OK);
       }catch (Exception e){
           e.printStackTrace();
           throw new ResourceCreationFailedException("could not add item to cart");
       }
    }


    @Transactional
    public HttpStatus updateItemQuantity(CartItemsUpdate update) {
        try {

            CartItems cartItem = cartItemsRepository.findById(update.getCartItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
            cartItem.setQuantity(update.getQuantity());
            cartItemsRepository.save(cartItem);

            log.info("success");
            return HttpStatus.OK;
        }catch (Exception e){
            throw new ResourceCreationFailedException("could not update item quantity");
        }
    }



    @Transactional
    public CartResponse getCartById(String userId, Pageable pageable) {

        Double totalPrice = 0.00;

        try {
            Users usr = usersRepository.findUsersByUserId(userId);
            Cart cart = cartRepository.findCartByUsers(usr).orElseThrow(
                    () -> new IllegalArgumentException("Cart not found"));

            CartResponse cartResponse = new CartResponse();
            cartResponse.setCartId(cart.getCartId());
            cartResponse.setUsername(cart.getUsers().getUsername());
            log.info("set the name successfully");



            // Map cart items to response
            List<CartItemsResponse> itemsResponses = new ArrayList<>();

            for (CartItems items : cart.getCartItems()) {
                CartItemsResponse itemsResponse = new CartItemsResponse();
                Products products = productsRepository.findByProductId(items.getProducts().getSpecificProductId());

                String pro = items.getProducts().getSpecificProductId();
                log.info(""+pro);
                SpecificProductDetails product = specificProductsRepository.findBySpecificProductId(items.getProducts().getSpecificProductId()).orElseThrow();
                log.info(""+products);
                itemsResponse.setCartItemId(items.getCartItemId());
                itemsResponse.setCount(items.getQuantity());
                itemsResponse.setProductId(product.getSpecificProductId());
                itemsResponse.setProductName(product.getProducts().getProductName());
                itemsResponse.setProductImageUrl(items.getProducts().getProductImagesList().get(0).getImageUrl());
                itemsResponse.setProductPrice(items.getProducts().getProductPrice()-items.getProducts().getDiscount());

                log.info("setting specific products");
                itemsResponse.setInStock(product.getCount() > 0);
                itemsResponse.setColor(product.getColor());
                log.info("done with this round");

                SpecificProductDetails details = specificProductsRepository.findBySpecificProductId(items.getProducts().getSpecificProductId()).orElseThrow();
                itemsResponse.setInStock(details.getCount() > 0);
                totalPrice = totalPrice + itemsResponse.getProductPrice();
                itemsResponses.add(itemsResponse);
            }

            cartResponse.setTotalPrice(totalPrice);
            cartResponse.setCartItemsResponses(itemsResponses);
            log.info("items set successfully");


            // Fetch "You May Like" products

            log.info("setting the products you may like");
            List<String> categoryIds = cart.getCartItems().stream()
                    .flatMap(item -> item
                             .getProducts()
                             .getProducts()
                             .getCategoriesList()
                             .stream()
                             .map(Categories
                                  ::getCategoryId))
                    .distinct()
                    .toList();

            Page<Products> productsPage = productsRepository.findAllByCategoriesList_CategoryIdIn(categoryIds, pageable);
            log.info("got the products "+productsPage);
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
            log.info("success");



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
        }catch (Exception e){
            throw new ResourceNotFoundException("could not get the cart items");
        }
    }

    @Transactional
    public HttpStatus removeItemFromCart(List<String> cartItemIds) throws EntityDeletionException {
        try {
            System.out.println(cartItemIds);
            if (cartItemIds.isEmpty()) {
                return HttpStatus.OK;
            }

            if (cartItemIds.size() == 1) {
                cartItemsRepository.deleteById(cartItemIds.get(0));
                log.info("deleted"+cartItemIds.get(0));
            }else {
                List<CartItems> items = new ArrayList<>();
                for (String cartItemId : cartItemIds) {
                    log.info("" + cartItemId);
                    CartItems item = cartItemsRepository.findCartItemsByCartItemId(cartItemId);
                    log.info("" + item);
                    items.add(item);
                }
                log.info("to be deleted :"+items);
                cartItemsRepository.deleteAll(items);
            }

            log.info("success");
            return HttpStatus.OK;
        }catch (Exception e){
            e.printStackTrace();
            throw new EntityDeletionException("could not delete the cart items");
        }
    }

    @Transactional
    public HttpStatus deleteCart(String cartId){
        try {
            Cart cart = cartRepository.findCartByCartId(cartId);
            cartRepository.delete(cart);
            return HttpStatus.OK ;
        }catch (Exception e){
            throw new EntityDeletionException("could not delete the cart");
        }
    }


}
