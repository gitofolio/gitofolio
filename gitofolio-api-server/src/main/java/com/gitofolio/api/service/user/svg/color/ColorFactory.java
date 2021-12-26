package com.gitofolio.api.service.user.svg.color;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.gitofolio.api.service.user.svg.color.colors.Color;

import java.util.List;

@Service
public class ColorFactory{
	
	@Autowired
	List<Color> colors;
	
	public Color getColor(String name){
		for(Color color : colors)
			if(color.getName().equals(name)) return color;
		return colors.get(0);
	}
	
}