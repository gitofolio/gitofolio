package com.gitofolio.api.repository.auth;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.domain.auth.*;

import java.util.Optional;
import java.util.NoSuchElementException;
import java.time.LocalDate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PersonalAccessTokenRepositoryTest{
	
	@Autowired
	private PersonalAccessTokenRepository personalAccessTokenRepository;
	
	@Test
	public void PersonalAccessTokenRepository_findByTokenValue_Test(){
		// given
		PersonalAccessToken token = this.getToken();
		
		// when
		this.saveToken(token);
		PersonalAccessToken result = this.personalAccessTokenRepository.findByTokenValue(token.getTokenValue()).get();
		
		// then
		assertEquals(result.getTokenValue(), token.getTokenValue());
	}
	
	@Test
	public void PersonalAccessTokenRepository_deleteUnusedToken_Test(){
		// given
		PersonalAccessToken token = this.getToken();
		
		// when
		this.saveToken(token);
		this.personalAccessTokenRepository.deleteUnusedToken(LocalDate.now().plusDays(1));
		
		// then
		assertThrows(NoSuchElementException.class, ()->this.personalAccessTokenRepository.findByTokenValue(token.getTokenValue()).get());
	}
	
	private void saveToken(PersonalAccessToken token){
		this.personalAccessTokenRepository.saveAndFlush(token);
	}
	
	private PersonalAccessToken getToken(){
		PersonalAccessToken personalAccessToken = new PersonalAccessToken();
		return personalAccessToken;
	}
	
}