package com.gitofolio.api.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.gitofolio.api.domain.user.PortfolioCard;

import java.util.*;

public interface PortfolioCardRepository extends JpaRepository<PortfolioCard, Long>{
	
	@Query("select p from PortfolioCard as p inner join p.userInfo as u where u.name = :name")
	List<PortfolioCard> findByName(@Param("name") String name);
	
	@Modifying
	@Query(value = "DELETE p FROM portfolio_card p JOIN user_info i WHERE i.name = :name", nativeQuery = true)
	void deleteByName(@Param("name") String name);
	
}