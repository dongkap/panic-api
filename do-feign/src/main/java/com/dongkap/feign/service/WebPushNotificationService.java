package com.dongkap.feign.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jose4j.lang.JoseException;

import com.dongkap.dto.notification.PushNotificationDto;
import com.dongkap.dto.notification.WebSubscriptionDto;

public interface WebPushNotificationService {
	
	public void subscribe(WebSubscriptionDto subscription, String username);

	public void privateMessage(PushNotificationDto message, String to) throws GeneralSecurityException, IOException, JoseException, ExecutionException, InterruptedException;

	public void broadcast(PushNotificationDto message, List<String> to) throws GeneralSecurityException, IOException, JoseException, ExecutionException, InterruptedException;

}
