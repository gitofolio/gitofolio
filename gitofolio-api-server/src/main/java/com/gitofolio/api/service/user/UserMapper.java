package com.gitofolio.api.service.user;

import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface UserMapper{
	
	UserDTO doMap(String name);
	UserDTO resolveMap(UserDTO userDTO);
	
}