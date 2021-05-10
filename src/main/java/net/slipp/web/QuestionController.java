package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.domain.Question;
import net.slipp.domain.QuestionRepository;
import net.slipp.domain.Result;
import net.slipp.domain.User;

@Controller
@RequestMapping("questions")
public class QuestionController {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@GetMapping("/form")
	public String form(HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			System.out.println(session.getAttribute("sessionedUser"));
			return "redirect:/users/login";
		}
		
		System.out.println(session.getAttribute("sessionedUser"));
		return "qna/form"; 	
	}
	
	
	@PostMapping("")
	public String create(String title, String contents, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session))
			return "redirect:/users/login";
		
		User sessionUser = HttpSessionUtils.getUserFromSession(session);
		Question newQuestion = new Question(sessionUser, title, contents);
		
		questionRepository.save(newQuestion);
		
		return "redirect:/";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable Long id, Model model) {
		
		Question question = questionRepository.findById(id).get();
		
		model.addAttribute("question", question);
		
		return "qna/show";
	}
	
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		
		Question question = questionRepository.findById(id).get();
		Result result = valid(session, question);
		if(!result.isValid()) {
			model.addAttribute("errorMessage", result.getErrorMessage());
			return "/user/login";
		}

		model.addAttribute("question", question);
		return "qna/updateForm";
		
	}
	
	private Result valid(HttpSession session, Question question) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return Result.fail("로그인 필요합니다. ");
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		if(!question.isSameWriter(loginUser)) {
			throw new IllegalStateException("자신의 게시글만 수정할 수 있습니다.");
		}
		
		return Result.ok();
	}
	
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id, Question newQuestion, HttpSession session, Model model) {
		
		Question question = questionRepository.findById(id).get();
		Result result = valid(session, question);
		if(!result.isValid()) {
			model.addAttribute("errorMessage", result.getErrorMessage());
			return "/user/login";
		}
		
		question.update(newQuestion);
		questionRepository.save(question);
		return String.format("redirect:/questions/%d", id);
		
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id, HttpSession session, Model model) {
		
		Question question = questionRepository.findById(id).get();
		Result result = valid(session, question);
		if(!result.isValid()) {
			model.addAttribute("errorMessage", result.getErrorMessage());
			return "/user/login";
		}
		
		questionRepository.delete(question);
		return "redirect:/";
		
	}
}








