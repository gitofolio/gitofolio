package com.gitofolio.api.service.user.svg.color.colors;

import org.springframework.stereotype.Component;

@Component
public class White extends Color{
	
	public White(){
		this.name = "white";
		this.mainColor = "#FFFFFF";
		this.textColor = "#2D2D2D";
		this.subColor = "#C4C4C4";
		this.pointColor = "#FFE381";
	}
	
}