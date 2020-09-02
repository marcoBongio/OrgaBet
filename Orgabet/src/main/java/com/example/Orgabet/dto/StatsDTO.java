package com.example.Orgabet.dto;

import java.util.List;

import com.example.Orgabet.models.Match;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class StatsDTO {
	private String id;
	private Double homeWin;
	private Double homeDraw;
	private Double homeLost;
	private Double homeOver;
	private Double homeUnder;
	private List<AvgDTO> avgOdds;
	
	
	public StatsDTO(String t,Double HW,Double HD, Double HL, Double HO, Double HU, List<AvgDTO> avg) {
		this.id = t;
		this.homeWin = HW;
		this.homeDraw = HD;
		this.homeLost = HL;
		this.homeOver = HO;
		this.homeUnder = HU;
		this.avgOdds = avg;
	}
}
