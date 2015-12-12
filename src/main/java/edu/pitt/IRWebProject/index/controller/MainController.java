package edu.pitt.IRWebProject.index.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.pitt.IRWebProject.index.bo.Search;
import edu.pitt.IRWebProject.index.services.SearchServices;

@Controller
@RequestMapping("/")
public class MainController {
	String message = "Welcome to Spring MVC!";

	@Autowired
	private SearchServices searchServices;

	/**
	 * map index.html
	 * 
	 * @return
	 */
	@RequestMapping("index.html")
	public String showIndex() {
		return "index";
	}

	/**
	 * map the tested hello world page
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("hello.html")
	public ModelAndView showMessage(
			@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
		ModelAndView mv = new ModelAndView("helloworld");
		mv.addObject("message", message);
		mv.addObject("name", name);
		return mv;
	}

	/**
	 * get search tip by input query
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping("searchTip.json")
	@ResponseBody
	public String getSearchTip(@RequestParam(value = "query", required = true) String query) {
		List<Search> list = searchServices.selectSearchByQueryLike(query);
		JSONArray result = new JSONArray();
		if (result != null) {
			for (Search search : list) {
				JSONObject jo = new JSONObject();
				jo.put("query", search.getQuery());
				result.put(jo);
			}
		}
		JSONObject jo = new JSONObject();
		jo.put("list", result);
		return jo.toString();
	}
}