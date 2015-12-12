package edu.pitt.IRWebProject.searchRecord.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import edu.pitt.IRWebProject.searchRecord.bo.SearchRecord;

@Repository
public interface SearchRecordDAO {
	public SearchRecord selectSearchByID(int searchID);

	public SearchRecord insertSearch(SearchRecord search);

	public List<SearchRecord> selectSearchByQueryLike(@Param("query") String query);
}
