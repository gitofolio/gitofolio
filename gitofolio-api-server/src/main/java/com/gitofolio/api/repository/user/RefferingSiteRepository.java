package com.gitofolio.api.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.gitofolio.api.domain.user.RefferingSite;
import com.gitofolio.api.domain.user.UserStatistics;

@Repository
public interface RefferingSiteRepository extends JpaRepository<RefferingSite, Long>{
	
	@Query("select r from RefferingSite r inner join r.userStatistics u where u.id = :id")
	List<RefferingSite> findByUserStatistics(@Param("id") Long id);
	
}