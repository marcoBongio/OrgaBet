package com.example.Orgabet.repositories;

import java.io.*;
import java.util.*;

import com.example.Orgabet.dto.AvgDTO;
import com.example.Orgabet.dto.StatsDTO;
import com.example.Orgabet.dto.countDTO;
import com.example.Orgabet.dto.divisionDTO;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import com.example.Orgabet.models.Match;

public class MatchRepositoryImpl implements MatchRepositoryCustom {
	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public MatchRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate =mongoTemplate;
	}
	
	@Override
	public List<divisionDTO> selectSortedDivisions(String date, String date2, String date3, String sport){
		MatchOperation filterSport = Aggregation.match(new Criteria("sport").is(sport));
		MatchOperation filterDate;
		if(!sport.equals("Tennis"))
			filterDate = Aggregation.match(new Criteria().orOperator(new Criteria("date").is(date), new Criteria("date").is(date2),  new Criteria("date").is(date3)));
		else filterDate = Aggregation.match(new Criteria("date").is(date));
		GroupOperation grp = Aggregation.group("sport","division");
		SortOperation srt = Aggregation.sort(Sort.Direction.ASC, "division");
		
		Aggregation aggr = Aggregation.newAggregation(filterSport, filterDate, grp, srt);
		
		List<divisionDTO> res = mongoTemplate.aggregate(aggr, Match.class, divisionDTO.class).getMappedResults();
		
		return res;
	}
	
	@Override
	public List<Match> selectSortedMatches(String sport, String date, String date2, String date3, String division) {
		MatchOperation filterSport = Aggregation.match(new Criteria("sport").is(sport));
		MatchOperation filterDate;
		if(!sport.equals("Tennis"))
			filterDate = Aggregation.match(new Criteria().orOperator(new Criteria("date").is(date), new Criteria("date").is(date2),  new Criteria("date").is(date3)));
		else filterDate = Aggregation.match(new Criteria("date").is(date));
		MatchOperation filterDiv = Aggregation.match(new Criteria("division").is(division));
	
		SortOperation srt = Aggregation.sort(Sort.Direction.ASC, "date");
		
		Aggregation aggr;
		if(sport.equals("Basket"))
			 aggr = Aggregation.newAggregation(filterSport, filterDate, srt);
		else aggr = Aggregation.newAggregation(filterSport, filterDate, filterDiv, srt);
		
		List<Match> res = mongoTemplate.aggregate(aggr, Match.class, Match.class).getMappedResults();
		return res;
	}
	
	@Override
	public List<AvgDTO> computeAverageOdds(String id) {
		MatchOperation filterMatch = Aggregation.match(new Criteria("id").is(id));
		UnwindOperation unw = Aggregation.unwind("odds");
		UnwindOperation unw2 = Aggregation.unwind("odds.quotes");
		
		GroupOperation grp = Aggregation.group("odds.type").avg("odds.quotes.odd").as("avg");
		
		ProjectionOperation proj = Aggregation.project("avg");
		
		Aggregation aggr = Aggregation.newAggregation(filterMatch, unw, unw2, grp);

		List<AvgDTO> res = mongoTemplate.aggregate(aggr, Match.class, AvgDTO.class).getMappedResults();
								//prima class = sorgente, seconda = destin
		return res;

	}
	
	@Override
	public List<countDTO> selectTeamsHome(String sport, Integer year, String division){
		MatchOperation filterSport = Aggregation.match(new Criteria("sport").is(sport));
		MatchOperation filterYear = Aggregation.match(new Criteria("year").is(year));
		
		MatchOperation filterDiv = null;
		if(sport.equals("Football"))
			 filterDiv = Aggregation.match(new Criteria("division").is(division));
	
		GroupOperation grp =  Aggregation.group("homeTeam").count().as("count");
		
		SortOperation srt = Aggregation.sort(Sort.Direction.ASC, "_id");
		
		Aggregation aggr;
		if(sport.equals("Football"))
			 aggr = Aggregation.newAggregation(filterSport, filterYear, filterDiv, grp, srt);
		else aggr = Aggregation.newAggregation(filterSport, filterYear, grp, srt);
		
		List<countDTO> res = mongoTemplate.aggregate(aggr, Match.class, countDTO.class).getMappedResults();
		
		return res;
	}
	
	@Override
	public List<countDTO> selectTeamsAway(String sport, Integer year,String division){
		MatchOperation filterSport = Aggregation.match(new Criteria("sport").is(sport));
		
		MatchOperation filterYear = Aggregation.match(new Criteria("year").is(year));
		
		MatchOperation filterDiv = null;
		if(sport.equals("Football"))
			 filterDiv = Aggregation.match(new Criteria("division").is(division));
		
		GroupOperation grp =  Aggregation.group("awayTeam").count().as("count");
		
		SortOperation srt = Aggregation.sort(Sort.Direction.ASC, "_id");
		
		Aggregation aggr;
		if(sport.equals("Football"))
			 aggr = Aggregation.newAggregation(filterSport, filterYear, filterDiv, grp, srt);
		else aggr = Aggregation.newAggregation(filterSport, filterYear, grp, srt);
		
		List<countDTO> res = mongoTemplate.aggregate(aggr, Match.class, countDTO.class).getMappedResults();
		
		return res;
	}
	
	@Override
	public List<countDTO> selectWinningTennisPlayer(Integer year, String surface){
		MatchOperation filterSport = Aggregation.match(new Criteria("sport").is("Tennis"));
		
		MatchOperation filterYear = Aggregation.match(new Criteria("year").is(year));

		MatchOperation filterSurface = Aggregation.match(new Criteria("surface").is(surface));
		
		GroupOperation grp =  Aggregation.group("homeTeam").count().as("count");
		
		SortOperation srt = Aggregation.sort(Sort.Direction.ASC, "_id");
		
		Aggregation aggr = Aggregation.newAggregation(filterSport, filterYear, filterSurface, grp, srt);
		
		List<countDTO> res = mongoTemplate.aggregate(aggr, Match.class, countDTO.class).getMappedResults();
		
		return res;
	}
	
	@Override
	public StatsDTO computeTennisPlayer(String surface, String player, Double totWin, Integer year) {
		Double winPerc = 100.00, lostPerc = 0.00;
		MatchOperation filterSport = Aggregation.match(new Criteria("sport").is("Tennis"));
		MatchOperation filterYear = Aggregation.match(new Criteria("year").is(year));
		MatchOperation filterSurface = Aggregation.match(new Criteria("surface").is(surface));
		MatchOperation filterPlayer = Aggregation.match(new Criteria("awayTeam").is(player));
		//count number of lost matches for a single player
		GroupOperation grp =  Aggregation.group("awayTeam").count().as("count");
		
		Aggregation aggr = Aggregation.newAggregation(filterSport, filterYear, filterSurface, filterPlayer, grp);
		
		List<countDTO> res = mongoTemplate.aggregate(aggr, Match.class, countDTO.class).getMappedResults();
		
		try {
			winPerc = (totWin / (totWin + res.get(0).getCount())) * 100;
		} catch(Exception e) {};
		
		lostPerc = 100.00 - winPerc;
		
		filterPlayer =  Aggregation.match(new Criteria("homeTeam").is(player));
		UnwindOperation unw = Aggregation.unwind("odds");
		UnwindOperation unw2 = Aggregation.unwind("odds.quotes");
		
		GroupOperation grp2 = Aggregation.group("odds.type").avg("odds.quotes.odd").as("avg");
		
		ProjectionOperation proj = Aggregation.project("avg");
		
		Aggregation aggr2 = Aggregation.newAggregation(filterSport, filterYear, filterSurface,filterPlayer, unw, unw2, grp2);
		
		List<AvgDTO> res2 = mongoTemplate.aggregate(aggr2, Match.class, AvgDTO.class).getMappedResults();
		
		StatsDTO stats = new StatsDTO(player, winPerc, null, lostPerc, null, null, res2);
		
		return stats;
	}
	
	@Override 
	public StatsDTO computeTeamHome(String division, String team, Double totHome, String sport, Integer year) {
		StatsDTO stats = null;
		double homeWin = 0.00, homeDraw = 0.00, homeLost = 0.00, homeOver = 0.00, homeUnder = 0.00;
		
		MatchOperation filterDiv = null;
		if(sport.equals("Football"))
			 filterDiv = Aggregation.match(new Criteria("division").is(division));
		MatchOperation filterTeam = Aggregation.match(new Criteria("homeTeam").is(team));
		MatchOperation filterHomeWin = Aggregation.match(new Criteria("fullTimeResult").is("H"));
		
		MatchOperation filterYear = Aggregation.match(new Criteria("year").is(year));
		
		GroupOperation grpHW =  Aggregation.group("homeTeam").count().as("count");
		Aggregation aggr;
		if(sport.equals("Football"))
			aggr = Aggregation.newAggregation(filterDiv, filterTeam, filterYear, filterHomeWin, grpHW);
		else aggr = Aggregation.newAggregation(filterTeam, filterYear, filterHomeWin, grpHW);
		
		List<countDTO> res = mongoTemplate.aggregate(aggr, Match.class, countDTO.class).getMappedResults();
		
		try {
			homeWin = ((res.get(0).getCount())/totHome) * 100;
			
		} catch(Exception e){
			
		}
		
		MatchOperation filterHomeDraw = Aggregation.match(new Criteria("fullTimeResult").is("D"));
		GroupOperation grpHD =  Aggregation.group("homeTeam").count().as("count");
		Aggregation aggr2;
		if(sport.equals("Football"))
			aggr2 = Aggregation.newAggregation(filterDiv, filterTeam, filterYear, filterHomeDraw, grpHD);
		else aggr2 = Aggregation.newAggregation(filterTeam, filterYear, filterHomeDraw, grpHD);
		
		List<countDTO> res2 = mongoTemplate.aggregate(aggr2, Match.class, countDTO.class).getMappedResults();
		
		try {
			homeDraw = ((res2.get(0).getCount())/totHome) * 100;
		} catch(Exception e){
			
		}
		
		MatchOperation filterHomeLost = Aggregation.match(new Criteria("fullTimeResult").is("A"));
		GroupOperation grpHL =  Aggregation.group("homeTeam").count().as("count");
		
		Aggregation aggr3;
		if(sport.equals("Football"))
			aggr3 = Aggregation.newAggregation(filterDiv, filterTeam, filterYear, filterHomeLost, grpHL);
		else aggr3 = Aggregation.newAggregation(filterTeam, filterYear, filterHomeLost, grpHL);
		
		List<countDTO> res3 = mongoTemplate.aggregate(aggr3, Match.class, countDTO.class).getMappedResults();
		
		try {
			homeLost = ((res3.get(0).getCount())/totHome) * 100;
		} catch(Exception e){
			
		}
		
		ProjectionOperation prjHO = Aggregation.project("id").and("fullTimeHomeScore").plus("fullTimeAwayScore").as("totScore");
		MatchOperation filterHomeOver = Aggregation.match(new Criteria("totScore").gt(2));
		GroupOperation grpHO = Aggregation.group().count().as("count");
		
		Aggregation aggr4;
		if(sport.equals("Football"))
			aggr4 = Aggregation.newAggregation(filterDiv, filterTeam, filterYear, prjHO, filterHomeOver, grpHO);
		else aggr4 = Aggregation.newAggregation(filterTeam, filterYear, filterHomeOver, grpHO);
		
		List<countDTO> res4 = mongoTemplate.aggregate(aggr4, Match.class, countDTO.class).getMappedResults();
		
		try {
			homeOver = ((res4.get(0).getCount())/totHome) * 100;
		} catch(Exception e){
			
		}
		
		homeUnder = 100.00 - homeOver;
		
		UnwindOperation unw = Aggregation.unwind("odds");
		UnwindOperation unw2 = Aggregation.unwind("odds.quotes");
		
		GroupOperation grp = Aggregation.group("odds.type").avg("odds.quotes.odd").as("avg");
		
		ProjectionOperation proj = Aggregation.project("avg");
		
		Aggregation aggr5;
		if(sport.equals("Football"))
			aggr5 = Aggregation.newAggregation(filterDiv, filterTeam, filterYear, unw, unw2, grp);
		else aggr5 = Aggregation.newAggregation(filterTeam, filterYear, unw, unw2, grp);

		List<AvgDTO> res5 = mongoTemplate.aggregate(aggr5, Match.class, AvgDTO.class).getMappedResults();
		
		stats = new StatsDTO(team, homeWin, homeDraw, homeLost, homeOver, homeUnder, res5);
		
		return stats;
	}
	
	@Override 
	public StatsDTO computeTeamAway(String division, String team, Double totAway, String sport, Integer year) {
		StatsDTO stats = null;
		double awayWin = 0.00, awayDraw = 0.00, awayLost = 0.00, awayOver = 0.00, awayUnder = 0.00;
		
		MatchOperation filterDiv = Aggregation.match(new Criteria("division").is(division));
		MatchOperation filterTeam = Aggregation.match(new Criteria("awayTeam").is(team));
		MatchOperation filterAwayWin = Aggregation.match(new Criteria("fullTimeResult").is("A"));
		
		MatchOperation filterYear = Aggregation.match(new Criteria("year").is(year));
		
		GroupOperation grpAW =  Aggregation.group("awayTeam").count().as("count");
		Aggregation aggr;
		if(sport.equals("Football"))
			aggr = Aggregation.newAggregation(filterDiv, filterTeam, filterYear, filterAwayWin, grpAW);
		else aggr = Aggregation.newAggregation(filterTeam, filterYear, filterAwayWin, grpAW);
		
		List<countDTO> res = mongoTemplate.aggregate(aggr, Match.class, countDTO.class).getMappedResults();
		
		try {
			awayWin = ((res.get(0).getCount())/totAway) * 100;
		} catch (Exception e) {
			
		}
		
		MatchOperation filterAwayDraw = Aggregation.match(new Criteria("fullTimeResult").is("D"));
		GroupOperation grpAD =  Aggregation.group("awayTeam").count().as("count");
		
		Aggregation aggr2;
		if(sport.equals("Football"))
			aggr2 = Aggregation.newAggregation(filterDiv, filterTeam, filterYear, filterAwayDraw, grpAD);
		else aggr2 = Aggregation.newAggregation(filterTeam, filterYear, filterAwayDraw, grpAD);
		
		List<countDTO> res2 = mongoTemplate.aggregate(aggr2, Match.class, countDTO.class).getMappedResults();
		
		try {
			awayDraw = ((res2.get(0).getCount())/totAway) * 100;
		} catch(Exception e){
			
		}
		
		MatchOperation filterAwayLost = Aggregation.match(new Criteria("fullTimeResult").is("H"));
		GroupOperation grpAL =  Aggregation.group("awayTeam").count().as("count");

		Aggregation aggr3;
		if(sport.equals("Football"))
			aggr3 = Aggregation.newAggregation(filterDiv, filterTeam, filterYear, filterAwayLost, grpAL);
		else aggr3 = Aggregation.newAggregation(filterTeam, filterYear, filterAwayLost, grpAL);
		
		List<countDTO> res3 = mongoTemplate.aggregate(aggr3, Match.class, countDTO.class).getMappedResults();
		
		try {
			awayLost = ((res3.get(0).getCount())/totAway) * 100;
		} catch(Exception e){
			
		}
		
		ProjectionOperation prjAO = Aggregation.project("id").and("fullTimeHomeScore").plus("fullTimeAwayScore").as("totScore");
		MatchOperation filterAwayOver = Aggregation.match(new Criteria("totScore").gt(2));
		GroupOperation grpAO = Aggregation.group().count().as("count");
		
		Aggregation aggr4;
		if(sport.equals("Football"))
			aggr4 = Aggregation.newAggregation(filterDiv, filterTeam, filterYear, prjAO, filterAwayOver, grpAO);
		else aggr4 = Aggregation.newAggregation(filterTeam, filterYear, filterAwayOver, grpAO);
		
		List<countDTO> res4 = mongoTemplate.aggregate(aggr4, Match.class, countDTO.class).getMappedResults();
		
		try {
			awayOver = ((res4.get(0).getCount())/totAway) * 100;
		} catch(Exception e){
			
		}
		
		awayUnder = 100.00 - awayOver;
		
		UnwindOperation unw = Aggregation.unwind("odds");
		UnwindOperation unw2 = Aggregation.unwind("odds.quotes");
		
		GroupOperation grp = Aggregation.group("odds.type").avg("odds.quotes.odd").as("avg");
		
		ProjectionOperation proj = Aggregation.project("avg");
		
		Aggregation aggr5;
		if(sport.equals("Football"))
			aggr5 = Aggregation.newAggregation(filterDiv, filterTeam, filterYear, unw, unw2, grp);
		else aggr5 = Aggregation.newAggregation(filterTeam, filterYear, unw, unw2, grp);
		
		List<AvgDTO> res5 = mongoTemplate.aggregate(aggr5, Match.class, AvgDTO.class).getMappedResults();
		
		stats = new StatsDTO(team, awayWin, awayDraw, awayLost, awayOver, awayUnder, res5);
		return stats;
	}
	
	@Override
	public void uploadFile(File f, List<Document> observationDocuments) {
		mongoTemplate.getCollection("match").insertMany(observationDocuments);
	}
}
