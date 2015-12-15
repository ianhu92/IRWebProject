package edu.pitt.IRWebProject.main.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class MainService {

	public ModelAndView returnErrorPage(int errorCode, String errorMessage) {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("errorCode", errorCode);
		mv.addObject("errorMessage", errorMessage);
		return mv;
	}
}
