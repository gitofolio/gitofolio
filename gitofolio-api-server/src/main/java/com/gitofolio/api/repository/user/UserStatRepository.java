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
	
	@Modifying
	@Query(value = "DELETE s FROM user_stat s JOIN user_info i WHERE i.name = :name", nativeQuery=true)
	void deleteByName(@Param("name") String name);
	
}