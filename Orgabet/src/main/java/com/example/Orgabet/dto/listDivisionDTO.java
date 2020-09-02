package com.example.Orgabet.dto;

import com.example.Orgabet.models.*;
import lombok.*;

import java.util.Iterator;
import java.util.List;

@ToString
@Getter
@Setter
public class listDivisionDTO
{
	private String sport;
	private String division;
	
	public listDivisionDTO(String sport, String division) {
		this.sport = division;
		this.division = division;
	}
}
