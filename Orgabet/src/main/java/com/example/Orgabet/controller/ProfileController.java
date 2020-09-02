package com.example.Orgabet.controller;

import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.Orgabet.dto.EditProfileDTO;
import com.example.Orgabet.dto.TableDTO;
import com.example.Orgabet.models.Coupon;
import com.example.Orgabet.models.User;
import com.example.Orgabet.repositories.UserRepository;
import com.example.Orgabet.services.MongoUserDetailsService;


@Controller
public class ProfileController
{
	@Autowired
	private MongoUserDetailsService userService;
	@Autowired
	private UserRepository repository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public Coupon findCouponById(User user, ObjectId id) {
		for(Iterator<Coupon> c = user.getCoupons().iterator(); c.hasNext();) {
			Coupon coupon = c.next();
			if(coupon.getId().equals(id))
				return coupon;
		}
		return null;
	}
	
    @GetMapping("admin/users/{username}")
    public String usersGet(@PathVariable("username") String username, Model model)
    {   
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User currentUser = userService.findUserByUsername(auth.getName());
		model.addAttribute("currentUser", currentUser);
    	User user=repository.findByUsername(username);
    	model.addAttribute("user", user);
    	model.addAttribute("coupons",user.getCoupons());
        return "profile";
    }
    
    @GetMapping("admin/users")
    public String usersGet(Model model)
    {   
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User currentUser = userService.findUserByUsername(auth.getName());
		model.addAttribute("currentUser", currentUser);
    	List<User> userList=repository.findAllBy();
    	model.addAttribute("userList",userList);
        return "userlist";
    }
    
    @GetMapping("/profile/{username}")
    public String profileGet(Model model)
    {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User user = userService.findUserByUsername(auth.getName());
    	model.addAttribute("currentUser", user);
    	model.addAttribute("user", user);
    	model.addAttribute("coupons",user.getCoupons());
        return "profile";
    }
    
    @RequestMapping(value = {"/profile/{username}", "/admin/users/{username}"}, method = RequestMethod.POST)
    public String profileEdit(@PathVariable("username") String username,EditProfileDTO edits, Model model)
    {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User currentUser = userService.findUserByUsername(auth.getName());
    	User user=repository.findByUsername(username);
    	user.setFirstName(edits.getFirstName());
    	user.setLastName(edits.getLastName());
    	user.setBanned(edits.isBanned());
    	user=repository.save(user);
    	model.addAttribute("currentUser", currentUser);
    	model.addAttribute("user", user);
    	model.addAttribute("coupons",user.getCoupons());
        return "profile";
    }
    
    @RequestMapping(value = {"/profile/{username}/coupon/{couponId}", "/admin/users/{username}/coupon/{couponId}"}, method = RequestMethod.GET)
    public String viewCoupon(@PathVariable("username") String username, @PathVariable("couponId") ObjectId couponId, Model model)
    {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User currentUser = userService.findUserByUsername(auth.getName());
    	User user=repository.findByUsername(username);
    	Coupon coupon = findCouponById(user,couponId);
    	model.addAttribute("currentUser", currentUser);
    	model.addAttribute("user", user);
    	model.addAttribute("coupon",coupon);
        return "coupon";
    }
    
    @RequestMapping(value = {"/deletecoupon/{username}/{couponId}"})
    public String deleteCoupon(@PathVariable("username") String username, @PathVariable("couponId") ObjectId couponId,Model model)
    {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User currentUser = userService.findUserByUsername(auth.getName());
    	User user=repository.findByUsername(username);
    	Coupon coupon = findCouponById(user,couponId);
    	List <Coupon> coupons=user.getCoupons();
    	coupons.remove(coupon);
    	user.setCoupons(coupons);
    	user=repository.save(user);
    	model.addAttribute("user", user);
    	model.addAttribute("coupons",coupons);
    	return "fragments :: couponList";
    }
    
    @RequestMapping(value = {"/user/{username}/delete"}, method = RequestMethod.POST)
    public ModelAndView profileEdit(@PathVariable("username") String username)
    {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User currentUser = userService.findUserByUsername(auth.getName());
    	Long success=repository.deleteUserByUsername(username);
    	
    	return new ModelAndView("redirect:/logout");
    }
    
    
}