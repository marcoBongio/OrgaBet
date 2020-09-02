package com.example.Orgabet.dto;

import com.example.Orgabet.models.*;
import lombok.*;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@ToString
@Getter
@Setter
public class TableDTO implements Comparable<TableDTO>
{
	private Match match;
	private List<AvgDTO> avg;
	
	public TableDTO(Match m, List<AvgDTO> a) {
		this.match = m;
		this.avg = a;
	}
	
	//returns quote of given type
	public Double getQuote(String type) {
		for(Iterator<AvgDTO> a = avg.iterator(); a.hasNext();) {
			AvgDTO quote = a.next();
			if(quote.getId().equals(type))
				return quote.getAvg();
		}
		return null;
	}
	
	@Override
	public int compareTo(TableDTO other)
	{
		String thisDate = this.getMatch().getDate();
		Integer day = Integer.parseInt(thisDate.substring(0,2));
		Integer month = Integer.parseInt(thisDate.substring(3,5));
		Integer year = Integer.parseInt(thisDate.substring(6,10));
		
		Date d1 = new Date(year, month, day);
		
		String otherDate = other.getMatch().getDate();
		day = Integer.parseInt(otherDate.substring(0,2));
		month = Integer.parseInt(otherDate.substring(3,5));
		year = Integer.parseInt(otherDate.substring(6,10));
		
		Date d2 = new Date(year, month, day);
		
		return d1.compareTo(d2);
	}
}