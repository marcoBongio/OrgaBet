package com.example.Orgabet.models;
import java.security.Timestamp;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class Tot implements Comparable<Tot> {
	private String bookmaker;
	private Double quoteTot;
	
	@Override
	public int compareTo(Tot o)
	{
		return (int)(o.getQuoteTot()-this.quoteTot);
	}
}
