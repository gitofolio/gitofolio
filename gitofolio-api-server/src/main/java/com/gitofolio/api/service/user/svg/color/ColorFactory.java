package com.gitofolio.api.service.user.svg.color;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.gitofolio.api.service.user.svg.color.colors.Color;
import com.gitofolio.api.service.user.factory.Factory;
import com.gitofolio.api.service.user.factory.parameter.StringParameter;

import java.util.List;

@Service
public class ColorFactory implements Factory<Color, StringParameter>{
	
	@Autowired
	List<Color> colors;
	
	@Override
	public Color get(StringParameter parameter){
		String name = parameter.getParameter();
		for(Color color : colors)
			if(color.getName().equals(name)) return color;
		return colors.get(0);
	}
	
}