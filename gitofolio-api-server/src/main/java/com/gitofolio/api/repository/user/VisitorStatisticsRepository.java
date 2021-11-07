package com.gitofolio.api.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.gitofolio.api.domain.user.VisitorStatistics;
import com.gitofolio.api.domain.user.UserStatistics;

@Repository
public interface VisitorStatisticsRepository extends JpaRepository<VisitorStatistics, Long>{
	
	@Query("select v from VisitorStatistics v inner join v.userStatistics u where u.id = :id")
	List<VisitorStatistics> findByUserStatistics(@Param("id") Long id);
	
}