package com.gitofolio.api.service.user.svg.color.colors;

import org.springframework.stereotype.Component;

@Component
public class Black extends Color{
	
	public Black(){
		this.name = "black";
		this.mainColor = "#2D2D2D";
		this.textColor = "#FFFFFF";
		this.subColor = "#C4C4C4";
		this.pointColor = "#FFFFFF";
	}
	
}