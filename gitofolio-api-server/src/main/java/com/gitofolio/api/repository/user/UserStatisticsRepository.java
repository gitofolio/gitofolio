package com.gitofolio.api.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gitofolio.api.domain.user.UserStatistics;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long>{
	
}