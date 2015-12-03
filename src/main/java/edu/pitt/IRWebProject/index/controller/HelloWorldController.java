package edu.pitt.IRWebProject.index.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
 
@Controller
@RequestMapping("/")
public class HelloWorldController {
	String message = "Welcome to Spring MVC!";
 
	@RequestMapping("index.html")
	public String showIndex(){
		return "index";
	}
	
	@RequestMapping("hello.html")
	public ModelAndView showMessage(
			@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
		ModelAndView mv = new ModelAndView("helloworld");
		mv.addObject("message", message);
		mv.addObject("name", name);
		return mv;
	}
}