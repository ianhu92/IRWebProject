package edu.pitt.IRWebProject.main.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
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

import edu.pitt.IRWebProject.lucene.LuceneService;
import edu.pitt.IRWebProject.lucene.Result;
import edu.pitt.IRWebProject.searchRecord.bo.SearchRecord;
import edu.pitt.IRWebProject.searchRecord.service.SearchRecordServices;

@Controller
@RequestMapping("/")
public class MainController {
	String message = "Welcome to Spring MVC!";

	@Autowired
	private SearchRecordServices searchRecordServices;

	@Autowired
	private LuceneService luceneService;

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
	 * map search.html
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("search.html")
	public ModelAndView showResult(@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page)
					throws Exception {
		if (query == null || "".equals(query)) {
			return new ModelAndView("redirect:./index.html");
		}

		query = URLDecoder.decode(query, "UTF-8");
		String[] terms;
		if (query.contains(" ")) {
			terms = query.split(" ");
		} else {
			terms = new String[1];
			terms[0] = query;
		}

		// insert search record
		Date date = new Date();
		int insertReturn = searchRecordServices.insertSearchRecord(new SearchRecord(query, date));
		if (insertReturn != 1) {
			// TODO: do something if insert failed
		}

		// do search
		Long start = System.currentTimeMillis();
		List<Result> resultList = luceneService.searchQuery(query);
		Long end = System.currentTimeMillis();
		System.out.println("Searched query \"" + query + "\" for " + (double)(end - start)/1000 + " seconds.");

		for (Result result : resultList) {
			// process url
			String url = result.getUrl();
			if (url != null && url.contains("zhihu.com")) {
				String originalUrl = URLEncoder.encode(url, "UTF-8");
				url = "http://www.microsofttranslator.com/bv.aspx?from=&to=en&a=" + originalUrl;
				result.setUrl(url);
			}

			// process answer
			String answer = result.getAnswer();
			if (answer != null) {
				// delete all html tags
				answer = answer.replaceAll("<[^>]*>", "");

				// trim within certain length
				if (answer.length() > 500) {
					int lastSpaceIndex = answer.lastIndexOf(" ", 500);
					answer = answer.substring(0, lastSpaceIndex) + " ...";
				}

				// set bold text with query terms
				for (String term : terms) {
					// TODO: long way to full logic
					answer.replaceAll(" " + term + " ", " <strong>" + term + "</strong> ");
				}

				result.setAnswer(answer);
			}
		}

		ModelAndView mv = new ModelAndView("search");
		mv.addObject("query", query);
		mv.addObject("resultList", resultList);
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
		List<SearchRecord> list = searchRecordServices.selectSearchByQueryLike(query);
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