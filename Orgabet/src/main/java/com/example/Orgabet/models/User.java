package com.example.Orgabet.models;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.*;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "user")
public class User {

  @Id 
  private String id;
  @Indexed(unique = true)
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  @Indexed(unique = true)
  private String email;
  @DBRef
  private Set<Role> roles;
  private boolean banned;
  private List<Coupon> coupons;
	
  	public User() {
  		this.coupons = new ArrayList<>(); //almeno le liste vanno istanziate altrimenti causa NullPointerException
	}
	public String getUsername() {
	  return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void addCoupon(Coupon c) {
		coupons.add(c);
		Collections.sort(coupons);
	}
	public List<Coupon> getCoupons() {
		return coupons;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public boolean isBanned() {
	return banned;}
	
}