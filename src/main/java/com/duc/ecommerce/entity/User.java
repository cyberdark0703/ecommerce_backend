package com.duc.ecommerce.entity;

import com.duc.ecommerce.security.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String password;

    @Enumerated(EnumType.STRING)
    Role role = Role.USER;

    @OneToMany(mappedBy = "user")
    List<Invoice> invoices;

}
