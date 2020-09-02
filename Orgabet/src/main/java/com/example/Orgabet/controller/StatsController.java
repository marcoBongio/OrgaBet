package com.example.Orgabet.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Orgabet.dto.AvgDTO;
import com.example.Orgabet.dto.StatsDTO;
import com.example.Orgabet.dto.TableDTO;
import com.example.Orgabet.dto.TableStatsDTO;
import com.example.Orgabet.dto.countDTO;
import com.example.Orgabet.models.Match;
import com.example.Orgabet.models.User;
import com.example.Orgabet.repositories.MatchRepository;
import com.example.Orgabet.services.MongoUserDetailsService;


@Controller
public class StatsController {
	
	@Autowired
	private MongoUserDetailsService userService;
	
	@Autowired
	MatchRepository statsRepository;
	
	@RequestMapping("/stats")
	public List<String> viewStats(@RequestParam(required = false, defaultValue = "Football", value="sport") String sport, @RequestParam(required = false, defaultValue = "I1", value="division")String division,@RequestParam(required = false, defaultValue = "2019", value="year") Integer year,Model model) {
		//due to inconsistency in the dataset we need to re-format the date string according
		//to the specific format of each sport
		
		/*if(sport.equals("Basket"))
			date = day+"/"+month+"/"+year;
		else if(sport.equals("Tennis"))
			date = year+"/"+month+"/"+day;
		else {
			if(year.equals("2018") || year.equals("2019"))
				date = day+"/"+month+"/"+year;
			else date = day+"/"+month+"/"+(year.substring(year.length() - 2));
		}*/
	
		
		List<countDTO> list = null, listA = null;
		if(sport.equals("Tennis"))
			list = statsRepository.selectWinningTennisPlayer(year, division);
		else {
			list = statsRepository.selectTeamsHome(sport, year, division);
			listA = statsRepository.selectTeamsAway(sport, year, division);
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		User currentUser = userService.findUserByUsername(auth.getName());
		model.addAttribute("currentUser", currentUser);
		
		List<TableStatsDTO> tbl = new ArrayList<TableStatsDTO>();
		List<TableStatsDTO> tblA = new ArrayList<TableStatsDTO>();
		
		for(Iterator<countDTO> l = list.iterator(); l.hasNext();) {
			countDTO cnt = l.next();
			StatsDTO stats = null;
			
			if(sport.equals("Tennis"))
				stats = statsRepository.computeTennisPlayer(division, cnt.getId(), cnt.getCount(), year);
			else
				stats = statsRepository.computeTeamHome(division,cnt.getId(), cnt.getCount(), sport, year);
		
			tbl.add(new TableStatsDTO(stats.getId(), stats.getHomeWin(), stats.getHomeDraw(), stats.getHomeLost(), stats.getHomeOver(), stats.getHomeUnder(), stats.getAvgOdds()));
		}
		
		model.addAttribute("statsH", tbl);
		
		if(!sport.equals("Tennis")) {
			for(Iterator<countDTO> l2 = listA.iterator(); l2.hasNext();) {
				countDTO cnt2 = l2.next();
				StatsDTO stats2 = statsRepository.computeTeamAway(division,cnt2.getId(), cnt2.getCount(), sport, year);
			
				tblA.add(new TableStatsDTO(stats2.getId(), stats2.getHomeWin(), stats2.getHomeDraw(), stats2.getHomeLost(), stats2.getHomeOver(), stats2.getHomeUnder(), stats2.getAvgOdds()));
			}
			
			model.addAttribute("statsA", tblA);
		}
		
		List<String> stats = new ArrayList<String>();
		stats.add("statsH");
		
		if(!sport.contentEquals("Tennis"))
			stats.add("statsA");
		
		model.addAttribute("sport", sport);
		model.addAttribute("division", division);
		model.addAttribute("year", year.toString());
		
		return stats;
	}
}