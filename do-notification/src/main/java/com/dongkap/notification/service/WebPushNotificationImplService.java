package com.dongkap.notification.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dongkap.dto.notification.NotificationPayloadDto;
import com.dongkap.dto.notification.PushNotificationDto;
import com.dongkap.dto.notification.WebSubscriptionDto;
import com.dongkap.feign.service.WebPushNotificationService;
import com.dongkap.notification.dao.WebSubscriptionRepo;
import com.dongkap.notification.entity.WebSubscriptionEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;

@Service("webPushNotificationService")
public class WebPushNotificationImplService implements WebPushNotificationService {
	
	@Value("${dongkap.vapid.private-key}")
	private String privateKey;
	
	@Value("${dongkap.vapid.public-key}")
	private String publicKey;
	
	@Autowired
	private WebSubscriptionRepo webSubscriptionRepo;

	@Autowired
	private ObjectMapper objectMapper;
	
	private static PushService pushService = new PushService();
    private static final int ONE_DAY_DURATION_IN_SECONDS = 86400;
    private static Long DEFAULT_TTL = Integer.toUnsignedLong(28) * ONE_DAY_DURATION_IN_SECONDS;
    
    @PostConstruct
    public void initKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
		pushService.setPublicKey(publicKey);
		pushService.setPrivateKey(privateKey);    	
    }

	@Override
	public void subscribe(WebSubscriptionDto subscription, String username) {
		WebSubscriptionEntity subs = webSubscriptionRepo.findByUsername(username);
		if(subs == null) {
			subs = new WebSubscriptionEntity();
		}
		subs.setEndpoint(subscription.getEndpoint());
		subs.setExpirationTime(DEFAULT_TTL);
		subs.setP256dh(subscription.getKeys().getP256dh());
		subs.setAuth(subscription.getKeys().getAuth());
		subs.setUsername(username);
		subs.setSubscribed(true);
		webSubscriptionRepo.save(subs);
	}

	@Override
	public void privateMessage(PushNotificationDto message, String to) throws GeneralSecurityException, IOException, JoseException, ExecutionException, InterruptedException {
		WebSubscriptionEntity subscription = webSubscriptionRepo.findByUsername(to);
		NotificationPayloadDto payload = new NotificationPayloadDto();
		payload.getNotification().setTitle(message.getTitle());
		payload.getNotification().setBody(message.getBody());
		payload.getNotification().setTag(message.getTag());
		payload.getNotification().setIcon(message.getIcon());
		payload.getNotification().setData(objectMapper.writeValueAsString(message.getData()));
		Notification notification = new Notification(
				subscription.getEndpoint(),
				subscription.getP256dh(),
				subscription.getAuth(),
				objectMapper.writeValueAsBytes(payload));
		pushService.sendAsync(notification);
	}

	@Override
	public void broadcast(PushNotificationDto message, List<String> to) throws GeneralSecurityException, IOException, JoseException, ExecutionException, InterruptedException {
		List<WebSubscriptionEntity> subscriptions = webSubscriptionRepo.findByUsernameIn(to);
		NotificationPayloadDto payload = new NotificationPayloadDto();
		payload.getNotification().setTitle(message.getTitle());
		payload.getNotification().setBody(message.getBody());
		payload.getNotification().setTag(message.getTag());
		payload.getNotification().setIcon(message.getIcon());
		payload.getNotification().setData(objectMapper.writeValueAsString(message.getData()));
		for(WebSubscriptionEntity subscription: subscriptions) {
			Notification notification = new Notification(
					subscription.getEndpoint(),
					subscription.getP256dh(),
					subscription.getAuth(),
					objectMapper.writeValueAsBytes(payload));
			pushService.sendAsync(notification);
		}
	}

}
