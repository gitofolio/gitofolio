package com.gitofolio.api.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gitofolio.api.domain.user.UserStat;

@Repository
public interface UserStatRepository extends JpaRepository<UserStat, Long>{
	
}