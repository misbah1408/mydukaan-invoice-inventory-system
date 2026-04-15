package com.mydukaan.repository;

import com.mydukaan.model.Store;
import com.mydukaan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByOwnerId(Long userId);

    @Query("""
        SELECT u FROM Store AS s
        RIGHT JOIN User AS u
        ON s.owner.id = u.id
    """)
    List<User> findAllUsers(Long storeId);
}
