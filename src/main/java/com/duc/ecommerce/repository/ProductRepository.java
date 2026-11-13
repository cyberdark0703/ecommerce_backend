package com.duc.ecommerce.repository;

import com.duc.ecommerce.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    boolean existsByNameContainingIgnoreCase(String name);

    @Query("""
    select p
    from Product p
    where p.price between :minPrice and :maxPrice
    and p.category.name = :categoryName 
    """)
    List<Product> findByPriceBetweenAndCategory (
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("categoryName") String categoryName
    );
    Optional<Product> findById(String id);
    Optional<Product> findByNameContainingIgnoreCase(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") String id);

}
