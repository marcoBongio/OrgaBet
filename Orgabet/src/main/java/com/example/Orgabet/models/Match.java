package com.example.Orgabet.models;
import com.example.Orgabet.dto.AvgDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.internal.connection.Time;

@ToString
@Getter
@Setter
@Document(collection = "match")
public class Match {
	@Id 
	private String id;
	@Indexed
	private String sport;
	@Indexed
	private String division;
	@Indexed
	private Integer year;
	private String date;//may cause problems if date is not correctly formatted in mongoDB
	private String time;
	@Indexed
	private String homeTeam;
	@Indexed
	private String awayTeam;
	private Integer fullTimeHomeScore;
	private Integer fullTimeAwayScore;
	private String fullTimeResult;
	private ArrayList<Odds> odds;
	
	//football-only attribute
	private Integer halfTimeHomeScore;
	private Integer halfTimeAwayScore;
	private String halfTimeResult;
	private Integer attendance;
	private String referee;
	private Integer homeShots;
	private Integer awayShots;
	private Integer homeOnTarget;
	private Integer awayOnTarget;
	private Integer homeWoodworks;
	private Integer awayWoodworks;
	private Integer homeCorners;
	private Integer awayCorners;
	private Integer homeFouls;
	private Integer awayFouls;
	private Integer homeFreeKicks;
	private Integer awayFreeKicks;
	private Integer homeOffsides;
	private Integer awayOffsides;
	private Integer homeYellowCards;
	private Integer awayYellowCards;
	private Integer homeRed;
	private Integer awayRed;
	private Integer homeBookingPoints;
	private Integer awayBookingPoints;
	
	//tennis-only attribute
	private Integer ATP;
	private String location;
	private String series;
	private String court;
	private String surface;
	private String round;
	private Integer bestOf;
	private Integer homeRank;
	private Integer awayRank;
	private Integer homeS1;
	private Integer awayS1;
	private Integer homeS2;
	private Integer awayS2;
	private Integer homeS3;
	private Integer awayS3;
	private Integer homeS4;
	private Integer awayS4;
	private Integer homeS5;
	private Integer awayS5;
	private String comment;
	
	//basket-only attribute
	private String season;
	private String typeOfGame;
	private String overUnder, firstQuarterResult, draw;
	private Integer totalScore, firstQuarterHomeScore, firstQuarterAwayScore, homeAwayDifference, drawHomeScore, drawAwayScore;
	
	//returns quote list of given bet type
	public ArrayList<Quotes> getQuoteList(String type) {
		for(Iterator<Odds> q = odds.iterator(); q.hasNext();) {
			Odds o = q.next();
			if(o.getType().equals(type))
				return o.getQuotes();
		}
		return null;
	}

}
