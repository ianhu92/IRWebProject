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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.memetix.mst.language.Language;

import edu.pitt.IRWebProject.lucene.service.LuceneService;
import edu.pitt.IRWebProject.main.service.MainService;
import edu.pitt.IRWebProject.language.bo.LanguageBO;
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
	private MainService mainService;

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
	 * @throws Exception
	 */
	@RequestMapping("index.html")
	public ModelAndView showIndex(
			@CookieValue(value = "language", required = false, defaultValue = "en") String languageShortName)
					throws Exception {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("currentLanguage", new LanguageBO(languageShortName));
		mv.addObject("optionLanguage",
				"en".equals(languageShortName) ? new LanguageBO("zh-CHS") : new LanguageBO("en"));
		return mv;
	}

	/**
	 * map error.html
	 */
	@RequestMapping("error.html")
	public ModelAndView showError() {
		return new ModelAndView("error");
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
			@CookieValue(value = "language", required = false, defaultValue = "en") String languageShortName)
					throws Exception {
		// insert search record
		Date date = new Date();
		searchRecordServices.insertSearchRecord(new SearchRecord(query, date));

		// deal with empty query
		if (query == null || "".equals(query)) {
			return mainService.returnErrorPage(1, "Invalid query.");
		}

		String queryEncode = URLEncoder.encode(query, "ISO-8859-1");
		String originalQuery = query = URLDecoder.decode(queryEncode, "UTF-8");

		// translate to english if necessary, so that we can search
		Language queryLanguage = Detect.execute(query);
		if (queryLanguage != Language.ENGLISH) {
			bingTranslatorService.setOriginLan(queryLanguage);
			bingTranslatorService.setDestLan(Language.ENGLISH);
			query = bingTranslatorService.translateQuery(query);
			if (query.contains("TranslateApiException")) {
				return mainService.returnErrorPage(3, "Translator error.");
			}
		}

		List<String> terms = luceneService.tokenizeString(new StandardAnalyzer(), query);

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
		mv.addObject("currentLanguage", new LanguageBO(languageShortName));
		mv.addObject("optionLanguage",
				"en".equals(languageShortName) ? new LanguageBO("zh-CHS") : new LanguageBO("en"));
		return mv;
	}

}