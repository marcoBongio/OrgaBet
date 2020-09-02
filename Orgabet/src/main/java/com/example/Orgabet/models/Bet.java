package com.example.Orgabet.models;
import java.lang.reflect.Array;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.Orgabet.dto.AvgDTO;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@ToString
@Getter
@Setter
@Document
public class Bet {
	private String matchId;
	private String homeTeam;
	private String awayTeam;
	private String result; //quote type (ex: H, D, OVER...)
	private Double avgOdd;
	private ArrayList<Quotes> quotes;
	
	public Bet() {
		this.quotes = new ArrayList<Quotes>();
	}
	
	public void addQuotes(ArrayList<Quotes> q) {
		this.quotes.addAll(q);
	}
}