package com.example.Orgabet.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.Orgabet.models.User;



public interface UserRepository extends MongoRepository<User, String> {

	User findByUsername(String username);
	User findByEmail(String email);
	Long deleteUserByUsername(String username);
	
	@Query(sort = "{ username : -1 }")
	List<User> findAllBy();
	
}
