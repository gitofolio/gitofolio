package com.gitofolio.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.gitofolio.api.domain.user.EncodedProfileImage;
import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.repository.user.EncodedProfileImageRepository;
import com.gitofolio.api.service.user.exception.NonExistUserException;

import java.util.Optional;
import java.util.Base64;
import java.net.URL;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;

@Service
public class EncodedProfileImageService{
	
	private EncodedProfileImageRepository encodedProfileImageRepository;
	
	public EncodedProfileImage get(UserInfo user){
		EncodedProfileImage encodedProfileImage = encodedProfileImageRepository.findByName(user.getName())
			.orElseThrow(()->new NonExistUserException("존재하지 않는 유저 입니다.", "유저이름을 확인해 주세요."));
		
		if(!encodedProfileImage.getProfileUrl().equals(user.getProfileUrl()))
			encodedProfileImage.setEncodedProfileUrl(this.getEncodedProfileImage(user.getProfileUrl()));
		
		return encodedProfileImage;
	}
	
	public EncodedProfileImage save(UserInfo user){
		EncodedProfileImage encodedProfileImage = this.encodedProfileImageRepository.findByName(user.getName())
			.orElseGet(()->this.getEncodedProfileImage(user));
		
		if(encodedProfileImage.getUserInfo() == null){
			encodedProfileImage.setUserInfo(user);
			this.encodedProfileImageRepository.save(encodedProfileImage);
		}
		
		if(!encodedProfileImage.getProfileUrl().equals(user.getProfileUrl())){
			this.getEncodedProfileImage(user);
			encodedProfileImage.setUserInfo(user);
		}
		
		return this.get(user);
	}
						 
	public void delete(String name){
		EncodedProfileImage encodedProfileImage = encodedProfileImageRepository.findByName(name)
			.orElseThrow(()->new NonExistUserException("존재하지 않는 유저에 대한 삭제 요청입니다.", "유저이름을 확인해 주세요."));
		
		this.encodedProfileImageRepository.deleteByName(name);
	}
	
	private EncodedProfileImage getEncodedProfileImage(UserInfo user){
		EncodedProfileImage encodedProfileImage = new EncodedProfileImage();
		encodedProfileImage.setProfileUrl(user.getProfileUrl());
		encodedProfileImage.setEncodedProfileUrl(getEncodedProfileImage(user.getProfileUrl()));
		return encodedProfileImage;
	}
	
	private String getEncodedProfileImage(String url){
		try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
			BufferedImage bufferedImage = ImageIO.read(new URL(url));
			
			bufferedImage = optimizeImage(bufferedImage);
		
			ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream);
			return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	private BufferedImage optimizeImage(BufferedImage bufferedImage){
		BufferedImage ans = new BufferedImage(60, 60, bufferedImage.getType());
		
		Graphics2D graphics2D = ans.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		graphics2D.drawImage(bufferedImage, 0, 0, 60, 60, null);
		graphics2D.dispose();
		
		return ans;
	}
	
	@Autowired
	public EncodedProfileImageService(EncodedProfileImageRepository encodedProfileImageRepository){
		this.encodedProfileImageRepository = encodedProfileImageRepository;
	}
	
}