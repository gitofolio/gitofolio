package com.gitofolio.api.service.user.svg.image;

import java.util.Base64;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

@Service
public class Base64ImageEncoder implements ImageEncoder{
	
	@Override
	public String encode(String url){
		try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
			BufferedImage bufferedImage = ImageIO.read(new URL(url));
			
			bufferedImage = optimizeImage(bufferedImage);
		
			ImageIO.write(bufferedImage, "JPEG", byteArrayOutputStream);
			
			return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	private BufferedImage optimizeImage(BufferedImage bufferedImage){
		BufferedImage ans = new BufferedImage(100, 100, bufferedImage.getType());
		
		Graphics2D graphics2D = ans.createGraphics();
		// graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		// graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		// graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		// graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		graphics2D.drawImage(bufferedImage, 0, 0, 100, 100, null);
		graphics2D.dispose();
		
		return ans;
	}
	
}