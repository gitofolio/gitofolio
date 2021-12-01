package com.gitofolio.api.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.EntityGraph;
	
import java.util.Optional;

import com.gitofolio.api.domain.user.UserStatistics;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long>{
	
	@Query("select distinct u from UserStatistics as u inner join u.userInfo as i on i.name = :name")
	Optional<UserStatistics> findByName(@Param("name") String name);
	
	@Modifying
	@Query(value = "DELETE s FROM user_statistics s JOIN user_info i WHERE i.name = :name", nativeQuery = true)
	void deleteByName(@Param("name") String name);
	
}