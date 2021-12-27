package com.gitofolio.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.domain.user.EncodedProfileImage;
import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.repository.user.EncodedProfileImageRepository;
import com.gitofolio.api.service.user.exception.NonExistUserException;
import com.gitofolio.api.service.user.svg.image.ImageEncoder;

import java.util.Optional;
import java.util.Base64;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;

@Service
public class EncodedProfileImageService{
	
	private EncodedProfileImageRepository encodedProfileImageRepository;
	
	private ImageEncoder base64ImageEncoder;
	
	@Transactional(readOnly=true)
	public EncodedProfileImage get(UserInfo user){
		EncodedProfileImage encodedProfileImage = encodedProfileImageRepository.findByName(user.getName())
			.orElseThrow(()->new NonExistUserException("존재하지 않는 유저 입니다.", "유저이름을 확인해 주세요."));
		
		if(!encodedProfileImage.getProfileUrl().equals(user.getProfileUrl())){
			encodedProfileImage = this.getEncodedProfileImage(user);
			encodedProfileImage.setUserInfo(user);
		}
		
		return encodedProfileImage;
	}
	
	@Transactional
	public EncodedProfileImage save(UserInfo user){
		EncodedProfileImage encodedProfileImage = this.encodedProfileImageRepository.findByName(user.getName())
			.orElseGet(()->this.getEncodedProfileImage(user));
		
		if(encodedProfileImage.getUserInfo() == null){
			encodedProfileImage.setUserInfo(user);
			this.encodedProfileImageRepository.save(encodedProfileImage);
		}
		else if(!encodedProfileImage.getProfileUrl().equals(user.getProfileUrl())){
			encodedProfileImage = this.getEncodedProfileImage(user);
			encodedProfileImage.setUserInfo(user);
		}
		
		return this.get(user);
	}
	
	@Transactional
	public void delete(String name){
		EncodedProfileImage encodedProfileImage = encodedProfileImageRepository.findByName(name)
			.orElseThrow(()->new NonExistUserException("존재하지 않는 유저에 대한 삭제 요청입니다.", "유저이름을 확인해 주세요."));
		
		this.encodedProfileImageRepository.deleteByName(name);
	}
	
	private EncodedProfileImage getEncodedProfileImage(UserInfo user){
		EncodedProfileImage encodedProfileImage = new EncodedProfileImage();
		encodedProfileImage.setProfileUrl(user.getProfileUrl());
		encodedProfileImage.setEncodedProfileUrl(this.base64ImageEncoder.encode(user.getProfileUrl()));
		return encodedProfileImage;
	}
	
	@Autowired
	public EncodedProfileImageService(EncodedProfileImageRepository encodedProfileImageRepository,
									 @Qualifier("base64ImageEncoder") ImageEncoder base64ImageEncoder){
		this.encodedProfileImageRepository = encodedProfileImageRepository;
		this.base64ImageEncoder = base64ImageEncoder;
	}
	
}