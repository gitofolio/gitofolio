package com.gitofolio.api.service.user.factory.parameter;

public class StringParameter implements ParameterAble{
	
	private String parameter;
	
	public StringParameter(String parameter){
		this.parameter = parameter;
	}
	
	public String getParameter(){
		return this.parameter;
	}
	
}