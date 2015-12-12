package edu.pitt.IRWebProject.index.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import edu.pitt.IRWebProject.index.bo.Search;

@Repository
public interface SearchDAO {
	public Search selectSearchByID(int searchID);

	public Search insertSearch(Search search);

	public List<Search> selectSearchByQueryLike(@Param("query") String query);
}
