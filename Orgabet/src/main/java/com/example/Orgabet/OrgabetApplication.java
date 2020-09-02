package com.example.Orgabet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.example.Orgabet.models.Role;
import com.example.Orgabet.repositories.MatchRepository;
import com.example.Orgabet.repositories.RoleRepository;

@SpringBootApplication
public class OrgabetApplication{

  public static void main(String[] args) throws Throwable {
    SpringApplication.run(OrgabetApplication.class, args);
  }
  @Bean
  CommandLineRunner init(RoleRepository roleRepository) {

      return args -> {

          Role adminRole = roleRepository.findByRole("ADMIN");
          if (adminRole == null) {
              Role newAdminRole = new Role();
              newAdminRole.setRole("ADMIN");
              roleRepository.save(newAdminRole);
          }

          Role userRole = roleRepository.findByRole("USER");
          if (userRole == null) {
              Role newUserRole = new Role();
              newUserRole.setRole("USER");
              roleRepository.save(newUserRole);
          }
      };

  }

}



