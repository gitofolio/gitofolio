package com.gitofolio.api.service.auth.oauth.applications.github;

import org.springframework.stereotype.Component;

@Component
public class GithubClientSecret{
	
	private String id = "b86796ef36e991c00490";
	private String secret = "a7a03f5a2a8293c9b07018da84326398fd68235a";
	
	public String getId(){
		return this.id;
	}
	
	public String getSecret(){
		return this.secret;
	}
	
}