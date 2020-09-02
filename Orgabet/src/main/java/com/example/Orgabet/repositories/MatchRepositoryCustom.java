package com.example.Orgabet.repositories;


import java.io.File;
import java.util.List;

import com.example.Orgabet.dto.AvgDTO;
import com.example.Orgabet.dto.StatsDTO;
import com.example.Orgabet.dto.countDTO;
import com.example.Orgabet.dto.divisionDTO;
import com.example.Orgabet.models.Match;
import org.bson.Document;


public interface MatchRepositoryCustom {
	
	//Function to select divisions to show up in a date
	List<divisionDTO> selectSortedDivisions(String date, String date2, String date3, String sport);
	//Functions for average odd aggregation
	List<Match> selectSortedMatches(String sport, String date, String date2, String date3, String division);
	List<AvgDTO> computeAverageOdds(String id);
	
	//Functions for statistics aggregations
	List<countDTO> selectTeamsHome(String sport, Integer year, String division);

	List<countDTO> selectTeamsAway(String sport, Integer year, String division);
	
	StatsDTO computeTeamHome(String division, String team, Double totHome, String sport, Integer year);

	StatsDTO computeTeamAway(String division, String team, Double totHome, String sport, Integer year);
	//Return list of winning tennis player and count of won matches
	List<countDTO> selectWinningTennisPlayer(Integer year, String surface);
	StatsDTO computeTennisPlayer(String surface, String player, Double totWin, Integer year);
	
	void uploadFile(File f, List<Document> doc);
}
