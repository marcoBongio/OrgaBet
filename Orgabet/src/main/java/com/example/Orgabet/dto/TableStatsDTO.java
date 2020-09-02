package com.example.Orgabet.dto;

import com.example.Orgabet.models.*;
import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
public class TableStatsDTO {
	public String team;
	public Double homeWin, homeDraw, homeLost, homeOver, homeUnder;
	public List<AvgDTO> avgOdds;
	
	public TableStatsDTO(String t, Double HW, Double HD, Double HL, Double HO, Double HU, List<AvgDTO> avg) {
		this.team = t;
		this.homeWin = HW;
		this.homeDraw = HD;
		this.homeLost = HL;
		this.homeOver = HO;
		this.homeUnder = HU;
		this.avgOdds = avg;
	}
}
