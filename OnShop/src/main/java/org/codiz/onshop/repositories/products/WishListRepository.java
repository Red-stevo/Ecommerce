package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.WishList;
import org.codiz.onshop.entities.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList,String> {
    WishList findByUser(Users users);
}
