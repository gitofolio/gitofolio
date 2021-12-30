package com.gitofolio.api.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

import com.gitofolio.api.domain.user.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long>{
	 
	Optional<UserInfo> findByName(String name);
	
	Optional<UserInfo> findById(Long id);
	
	@Modifying
	@Query("delete from UserInfo u where u.name=:name")
	void deleteByName(@Param("name") String name);
	
}