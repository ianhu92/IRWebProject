package edu.pitt.IRWebProject.index.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pitt.IRWebProject.index.bo.Search;
import edu.pitt.IRWebProject.index.dao.SearchDAO;

@Service
public class SearchServices {
	@Autowired
	private SearchDAO searchDAO;

	/**
	 * get Search by searchID
	 * 
	 * @param searchID
	 * @return
	 */
	public Search selectSearchBySearchID(int searchID) {
		return searchDAO.selectSearchByID(searchID);
	}

	public List<Search> selectSearchByQueryLike(String query) {
		return searchDAO.selectSearchByQueryLike(query);
	}
}
