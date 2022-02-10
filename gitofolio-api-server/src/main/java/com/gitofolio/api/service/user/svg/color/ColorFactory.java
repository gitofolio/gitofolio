package com.gitofolio.api.service.user.svg.color;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.gitofolio.api.service.user.svg.color.colors.Color;
import com.gitofolio.api.service.factory.Factory;

import java.util.List;

@Service
public class ColorFactory implements Factory<Color, String>{
	
	private List<Color> colors;

	@Override
	public Color get(String colorName){
		for(Color color : colors)
			if(color.getName().equals(colorName)) return color;
		return colors.get(0);
	}
	
	@Autowired
	public ColorFactory(List<Color> colors){
		this.colors = colors;
	}

}
