package com.example.Orgabet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.Orgabet.models.Role;
import com.example.Orgabet.models.User;
import com.example.Orgabet.repositories.RoleRepository;
import com.example.Orgabet.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Component
public class MongoUserDetailsService implements UserDetailsService{
  @Autowired
  private UserRepository repository;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  
  public User findUserByUsername(String username) {
	    return repository.findByUsername(username);
	}
  public User findUserByEmail(String mail) {
	    return repository.findByEmail(mail);
	}
  
  public void saveUser(User user) {
	    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	    Role userRole = roleRepository.findByRole("USER");
	    user.setRoles(new HashSet<>(Arrays.asList(userRole)));
	    repository.save(user);
	}
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      User user = repository.findByUsername(username);
      if(user != null && (  ( !user.isBanned() ) || (user.getUsername()=="Admin"))) {
          List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
          return buildUserForAuthentication(user, authorities);
      } else {
          throw new UsernameNotFoundException("username not found");
      }
  }
  
  private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
	    Set<GrantedAuthority> roles = new HashSet<>();
	    userRoles.forEach((role) -> {
	        roles.add(new SimpleGrantedAuthority(role.getRole()));
	    });

	    List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
	    return grantedAuthorities;
	}
  
  private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
	    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}
}