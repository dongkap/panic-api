package com.dongkap.feign.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

import org.jose4j.lang.JoseException;

import com.dongkap.dto.notification.PushNotificationDto;
import com.dongkap.dto.notification.WebSubscriptionDto;

public interface WebPushNotificationService {
	
	public void subscribe(WebSubscriptionDto subscription, String username);
	
	public void notify(PushNotificationDto message, String username) throws GeneralSecurityException, IOException, JoseException, ExecutionException, InterruptedException;

}
