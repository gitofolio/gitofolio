package com.gitofolio.api.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.gitofolio.api.domain.user.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long>{
	
	Optional<UserInfo> findByName(String name);
	
}

