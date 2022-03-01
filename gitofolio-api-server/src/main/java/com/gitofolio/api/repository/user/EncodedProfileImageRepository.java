package com.gitofolio.api.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.gitofolio.api.domain.user.EncodedProfileImage;

import java.util.Optional;

@Repository
public interface EncodedProfileImageRepository extends JpaRepository<EncodedProfileImage, Long>{
	
	@Query("select e from EncodedProfileImage e inner join e.userInfo i where i.name = :name")
	Optional<EncodedProfileImage> findByName(@Param("name") String name);
	
	@Modifying
	@Query(value = "DELETE e FROM encoded_profile_image as e WHERE e.user_info_id = (SELECT user_info_id FROM user_info as u WHERE u.name = :name)", nativeQuery = true)
	void deleteByName(@Param("name") String name);
	
}