package com.bluestone.app.social;

import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;

public class OAuthServiceProvider {
	
	private OAuthService oAuthService;

	public OAuthServiceProvider(OAuthServiceConfig config) {		
		ServiceBuilder serviceBuilder = new ServiceBuilder();
		serviceBuilder = serviceBuilder.provider(config.getApiClass()).apiKey(config.getApiKey()).apiSecret(config.getApiSecret()).scope(config.getScope()).callback(config.getCallback());
		oAuthService = serviceBuilder.build();
	}

	public OAuthService getService() {
		return oAuthService;
	}

}
