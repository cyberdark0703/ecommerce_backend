package com.duc.ecommerce.repository;
import com.duc.ecommerce.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    //Optional<User> findbyId(String id);

    List<User> findByNameContainingIgnoreCase(String keyword);
    List<User> findAllByOrderByNameAsc ();

    @Query("""
            select u from User u
            where u.id > :cursor
            order by u.id asc
            """)
    List<User> findNextUsers (
            @Param("cursor") int cursor,
            Pageable pageable
    );

    Optional<User> findByName(String name);

}
