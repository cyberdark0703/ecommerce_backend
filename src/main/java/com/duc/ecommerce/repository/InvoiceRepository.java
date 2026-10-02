package com.duc.ecommerce.repository;

import com.duc.ecommerce.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,String> {
    Optional<Invoice> findById(String id);

    @Query("""
    select distinct i
    from Invoice i
    join fetch i.user
    join fetch i.products p
    join fetch p.category
""")
    List<Invoice> findAllWithDetails();
}
