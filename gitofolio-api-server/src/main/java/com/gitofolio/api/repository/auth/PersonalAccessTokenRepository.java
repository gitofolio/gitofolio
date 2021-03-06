package com.gitofolio.api.repository.auth;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import com.gitofolio.api.domain.auth.PersonalAccessToken;

import java.util.Optional;
import java.time.LocalDate;

@Repository
public interface PersonalAccessTokenRepository extends JpaRepository<PersonalAccessToken, Long>{
	
	Optional<PersonalAccessToken> findByTokenKey(Long tokenKey);
	
	Optional<PersonalAccessToken> findByTokenValue(String tokenValue);
	
	@Modifying
	@Query("delete from PersonalAccessToken p where p.lastUsedDate<:tokenLifeCycle")
	void deleteUnusedToken(@Param("tokenLifeCycle") LocalDate tokenLifeCycle);
	
	@Modifying
	@Query(value="DELETE p FROM personal_access_token as p WHERE p.user_info_id = (SELECT user_info_id FROM user_info as u WHERE u.name = :name)", nativeQuery=true)
	void deleteByName(@Param("name") String name);
	
}