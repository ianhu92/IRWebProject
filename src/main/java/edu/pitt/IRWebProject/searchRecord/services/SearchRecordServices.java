package edu.pitt.IRWebProject.searchRecord.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pitt.IRWebProject.searchRecord.bo.SearchRecord;
import edu.pitt.IRWebProject.searchRecord.dao.SearchRecordDAO;

@Service
public class SearchRecordServices {
	@Autowired
	private SearchRecordDAO searchDAO;

	/**
	 * get Search by searchID
	 * 
	 * @param searchID
	 * @return
	 */
	public SearchRecord selectSearchBySearchID(int searchID) {
		return searchDAO.selectSearchByID(searchID);
	}

	public List<SearchRecord> selectSearchByQueryLike(String query) {
		return searchDAO.selectSearchByQueryLike(query);
	}
}
