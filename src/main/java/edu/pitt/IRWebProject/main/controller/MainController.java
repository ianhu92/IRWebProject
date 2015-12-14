package edu.pitt.IRWebProject.main.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.memetix.mst.language.Language;

import edu.pitt.IRWebProject.lucene.service.LuceneService;
import edu.pitt.IRWebProject.language.service.BingTranslatorService;
import edu.pitt.IRWebProject.lucene.bo.Result;
import edu.pitt.IRWebProject.lucene.bo.ResultList;
import edu.pitt.IRWebProject.searchRecord.bo.SearchRecord;
import edu.pitt.IRWebProject.searchRecord.service.SearchRecordServices;
import com.memetix.mst.detect.Detect;

/**
 * main controller for ".html" page requests
 * 
 * @author Ian
 *
 */
@Controller
@RequestMapping("/")
public class MainController {
	@Autowired
	private SearchRecordServices searchRecordServices;

	@Autowired
	private LuceneService luceneService;

	@Autowired
	private BingTranslatorService bingTranslatorService;

	/*
	 * public void initBingtranslatorService() {// initialize bingTranslatorService
	 * this.bingTranslatorService = new BingTranslatorService("ENGLISH", "CHINESE_SIMPLIFIED");
	 * }
	 */

	/**
	 * map index.html
	 * 
	 * @return
	 */
	@RequestMapping("index.html")
	public ModelAndView showIndex(
			@RequestParam(value = "lan", required = false, defaultValue = "en") String lan) {
		ModelAndView mv = new ModelAndView("index");
		return mv;
	}

	/**
	 * map search.html
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("search.html")
	public ModelAndView showResult(@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "lan", required = false, defaultValue = "en") String lan)
					throws Exception {
		if (query == null || "".equals(query)) {
			return new ModelAndView("redirect:./index.html");
		}

		String queryEncode = URLEncoder.encode(query, "ISO-8859-1");
		query = URLDecoder.decode(queryEncode, "UTF-8");

		// translate if necessary
		Language targetLanguage;
		if ("en".equals(lan)) {
			targetLanguage = Language.ENGLISH;
		} else if ("zh_cn".equals(lan)) {
			targetLanguage = Language.CHINESE_SIMPLIFIED;
		} else {
			targetLanguage = Language.ENGLISH;
		}
		Language queryLanguage = Detect.execute(query);
		if (targetLanguage != queryLanguage) {
			bingTranslatorService.setOriginLan(queryLanguage);
			bingTranslatorService.setDestLan(targetLanguage);
			query = bingTranslatorService.translateQuery(query);
		}
		List<String> terms = luceneService.tokenizeString(new StandardAnalyzer(), query);

		// insert search record
		Date date = new Date();
		int insertReturn = searchRecordServices.insertSearchRecord(new SearchRecord(query, date));
		if (insertReturn != 1) {
			// TODO: do something if insert failed
		}

		// do search
		Long start = System.currentTimeMillis();
		ResultList resultListObj = luceneService.searchQuery(query, page);
		List<Result> resultList = resultListObj.getResults();
		int totalPage = resultListObj.getTotalPage();
		int totalDoc = resultListObj.getTotalDoc();

		Long end = System.currentTimeMillis();
		System.out.println("Searched query \"" + query + "\" for " + (double) (end - start) / 1000
				+ " seconds.");

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
				/*
				 * String[] terms;
				 * if (query.contains(" ")) {
				 * terms = query.split(" ");
				 * } else {
				 * terms = new String[1];
				 * terms[0] = query;
				 * }
				 */

				for (String term : terms) {
					Pattern pattern = Pattern
							.compile("(^|[^a-zA-Z0-9>])" + term + "($|[^a-zA-Z0-9<])");
					Matcher matcher = pattern.matcher(answer);
					while (matcher.find()) {
						String substr = matcher.group();
						answer = matcher
								.replaceFirst(substr.replaceFirst(term, "<b>" + term + "</b>"));
						matcher = pattern.matcher(answer);
					}
				}
				result.setAnswer(answer);
			}
		}

		ModelAndView mv = new ModelAndView("search");
		mv.addObject("query", query);
		mv.addObject("queryEncode", queryEncode);
		mv.addObject("resultList", resultList);
		mv.addObject("currentPage", page);
		mv.addObject("totalPage", totalPage);
		mv.addObject("totalDoc", totalDoc);
		return mv;
	}

}