package net.slipp.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.domain.User;
import net.slipp.domain.UserRepository;

@Controller
@RequestMapping("users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("")
	public String create(User user) {
		System.out.println("user: " + user);
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@GetMapping("")
	public String list(Model model) {
		List<User> users = userRepository.findAll();
		model.addAttribute("users", users);
		return 	"user/list";
	}
	
	@GetMapping("/form")
	public String form() {
		return "user/form";
	}
	
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		
		if(HttpSessionUtils.isLoginUser(session)) {
			return "redirect:/users/login";	
		}
		
		User sessionedUser = HttpSessionUtils.getUserFromSession(session);
		if(id != sessionedUser.getId()) {
			throw new IllegalStateException("자신의 정보만 수정할 수 있습니다.");
		}
		
		User user = userRepository.findById(id).get();
		
		model.addAttribute("user", user);
		return "user/updateForm";
	}
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id, User newUser, HttpSession session) {
		
		if(HttpSessionUtils.isLoginUser(session)) {
			return "redirect:/users/login";	
		}
		
		User sessionedUser = HttpSessionUtils.getUserFromSession(session);
		if(id != sessionedUser.getId()) {
			throw new IllegalStateException("자신의 정보만 수정할 수 있습니다.");
		}
		
		
		User user = userRepository.findById(id).get();
		user.update(newUser);
		userRepository.save(user);
		
		return "redirect:/users";
	}
	
	
	@GetMapping("/login")
	public String login() {
		return "user/login";
	}
	
	@PostMapping("/login")
	public String login(String userId, String password, HttpSession session) {
		User user = userRepository.findByUserId(userId);
		if(user == null) {
			System.out.println("Login Failure!");
			return "redirect:/users/login";
		}
		
		if(!user.matchPassword(password)) {
			System.out.println("Login Failure!");
			return "redirect:/users/login";
		}			
		System.out.println("Login Success!");
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		
		return "redirect:/";
	}
	

	
	
}
















