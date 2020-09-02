package com.example.Orgabet.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AggregationDTO {
	private String homeTeam;
	private String awayTeam;
	private List<AvgDTO> home;
	private List<AvgDTO> draw;
	private	List<AvgDTO> away;
	private List<AvgDTO> over;
	private List<AvgDTO> under;
}
