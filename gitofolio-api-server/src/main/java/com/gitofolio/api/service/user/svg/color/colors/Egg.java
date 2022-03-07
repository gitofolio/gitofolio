package com.gitofolio.api.service.user.svg.color.colors;

import org.springframework.stereotype.Component;

@Component
public class Egg extends Color{
	
	public Egg(){
		this.name = "egg";
		this.mainColor = "rgba(255,255,255,0.01)";
		this.textColor = "#2D2D2D";
		this.subColor = "#FFD233";
		this.pointColor = "#2D2D2D";
		this.backgroundImage = "egg";
	}
	
}