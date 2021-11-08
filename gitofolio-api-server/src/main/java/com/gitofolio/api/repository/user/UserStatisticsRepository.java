package com.gitofolio.api.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import com.gitofolio.api.domain.user.UserStatistics;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long>{

	@Query("select u from UserStatistics as u inner join u.userInfo as i where i.name = :name")
	Optional<UserStatistics> findByName(@Param("name") String name);
	
}