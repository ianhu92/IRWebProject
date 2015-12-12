package edu.pitt.IRWebProject.main.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import edu.pitt.IRWebProject.main.bo.Search;

@Repository
public interface SearchDAO {
	public Search selectSearchByID(int searchID);

	public Search insertSearch(Search search);

	public List<Search> selectSearchByQueryLike(@Param("query") String query);
}
