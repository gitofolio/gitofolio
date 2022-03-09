package com.gitofolio.api.repository.common;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.gitofolio.api.domain.common.TodayInteraction;


@Repository
public interface TodayInteractionRepository extends JpaRepository<TodayInteraction, String>{
	
}