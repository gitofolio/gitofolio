package com.gitofolio.api.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

import com.gitofolio.api.domain.user.UserStat;

@Repository
public interface UserStatRepository extends JpaRepository<UserStat, Long>{
	
	@Query("select u from UserStat u inner join u.userInfo i where i.name = :name")
	Optional<UserStat> findByName(@Param("name") String name);
	
	@Query("select u from UserStat u inner join u.userInfo i where i.id = :id")
	Optional<UserStat> findById(@Param("id") Long id);
	
	@Modifying
	@Query(value = "DELETE s FROM user_stat as s WHERE s.user_info_id = (SELECT user_info_id FROM user_info as u WHERE u.name = :name)", nativeQuery=true)
	void deleteByName(@Param("name") String name);
	
}