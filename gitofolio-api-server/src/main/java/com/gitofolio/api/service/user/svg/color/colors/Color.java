package com.gitofolio.api.service.user.svg.color.colors;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class Color{
	
	protected String name;
	protected String mainColor;
	protected String pointColor;
	protected String textColor;
	protected String subColor;
	protected List<String> additionalColor;
	
	public String getName(){
		return this.name;
	}
	
	public String getMainColor(){
		return this.mainColor;
	}
	
	public String getPointColor(){
		return this.pointColor;
	}
	
	public String getTextColor(){
		return this.textColor;
	}
	
	public String getSubColor(){
		return this.subColor;
	}
	
	public String getAdditionalColor(int idx){
		return this.additionalColor.get(idx);
	}
	
}