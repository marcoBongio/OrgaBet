package com.example.Orgabet.models;
import java.lang.reflect.Array;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@ToString
@Getter
@Setter
@Document
public class Odds {
	private String type;
	private ArrayList<Quotes> quotes;
	
	public Odds(
			final String type,
			final ArrayList<Quotes> quotes) {
		this.type = type;
		this.quotes = quotes;
	}
}
