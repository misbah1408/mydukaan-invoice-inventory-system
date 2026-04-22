package com.mydukaan.repository;

import com.mydukaan.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStoreId(Long storeId);

    @Query("""
            SELECT p from Product as p WHERE p.store.id = :storeId
                    AND (
                          LOWER(p.name) LIKE LOWER(CONCAT('%', :text, '%'))
                          OR LOWER(p.category) LIKE LOWER(CONCAT('%', :text, '%'))                                                                               )
            """)
    List<Product> searchProductsByStoreIdAndNameOrCategory(Long storeId, String text);

//    works as same as above
//    List<Product> findByStoreIdAndNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
//            Long storeId, String name, String category);
}
