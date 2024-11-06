package org.codiz.onshop.service.impl.cart;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.codiz.onshop.entities.cart.Cart;
import org.codiz.onshop.entities.cart.Status;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.cart.CartRepository;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.serv.products.cart.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartImpl implements CartService {

    private final CartRepository cartRepository;
    private final UsersRepository usersRepository;

    public EntityCreationResponse createCart(String userId) {

        Users user = usersRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        Cart cart = new Cart();
        cart.setCreatedAt(Instant.now());
        cart.setStatus(Status.ACTIVE);
        cart.setUpdatedAt(Instant.now());
        cart.setUsers(user);
        cart.setCartItems(List.of());

        cartRepository.save(cart);

        EntityCreationResponse response = new EntityCreationResponse();
        response.setMessage("Successfully created cart.");
        response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.OK);
        return response;
    }


}
