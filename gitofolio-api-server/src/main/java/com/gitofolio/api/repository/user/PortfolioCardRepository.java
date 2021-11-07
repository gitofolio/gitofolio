package com.gitofolio.api.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import com.gitofolio.api.domain.user.PortfolioCard;

import java.util.List;

public interface PortfolioCardRepository extends JpaRepository<PortfolioCard, Long>{
	
	@Query("select p from PortfolioCard as p inner join p.userInfo as u where u.name = :name")
	List<PortfolioCard> findByName(@Param("name") String name, Pageable pageable);
	
	@Query("select p from PortfolioCard as p inner join p.userInfo as u where u.name = :name")
	List<PortfolioCard> findByName(@Param("name") String name);
}