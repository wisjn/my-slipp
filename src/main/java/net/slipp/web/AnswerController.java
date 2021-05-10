package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.domain.Answer;
import net.slipp.domain.AnswerRepository;
import net.slipp.domain.Question;
import net.slipp.domain.QuestionRepository;
import net.slipp.domain.User;

@Controller
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {
	
	@Autowired
	AnswerRepository answerRepository;
	
	@Autowired
	QuestionRepository questionRepository;
	
	@PostMapping("")
	public String create(@PathVariable Long questionId, HttpSession session, String contents) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return "redirect:/users/login";
		}
		User sessionedUser = HttpSessionUtils.getUserFromSession(session);
		Question question = questionRepository.findById(questionId).get();
		
		Answer answer = new Answer(sessionedUser, question, contents);
		answerRepository.save(answer);
		
		return String.format("redirect:/questions/%d", questionId);
	}
	
	@PutMapping("/{answerId}")
	public String update(@PathVariable Long answerId, @PathVariable Long questionId,HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return "redirect:/users/login";
		}
		
		Answer answer = answerRepository.findById(answerId).get();
		
		return String.format("redirect:/questions/%d", questionId);
	}
}






