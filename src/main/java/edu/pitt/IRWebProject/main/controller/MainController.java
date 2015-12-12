package edu.pitt.IRWebProject.main.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.pitt.IRWebProject.searchRecord.bo.SearchRecord;
import edu.pitt.IRWebProject.searchRecord.services.SearchRecordServices;

@Controller
@RequestMapping("/")
public class MainController {
	String message = "Welcome to Spring MVC!";

	@Autowired
	private SearchRecordServices searchServices;

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
	 * map ResultPage.html
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("search.html")
	public ModelAndView showResult(@RequestParam(value = "query", required = true) String query)
			throws UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView("search");
		query = URLDecoder.decode(query, "UTF-8");
		mv.addObject("query", query);
		return mv;
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
	public String getSearchTip(HttpServletResponse response,
			@RequestParam(value = "query", required = true) String query) {
		List<SearchRecord> list = searchServices.selectSearchByQueryLike(query);
		JSONArray result = new JSONArray();
		if (result != null) {
			for (SearchRecord search : list) {
				JSONObject jo = new JSONObject();
				jo.put("query", search.getQuery());
				result.put(jo);
			}
		}
		JSONObject jo = new JSONObject();
		jo.put("list", list);
		return jo.toString();
	}
}