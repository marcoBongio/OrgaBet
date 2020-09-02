package com.example.Orgabet.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Orgabet.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);
}
