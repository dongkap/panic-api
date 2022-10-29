package com.dongkap.notification.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dongkap.common.aspect.ResponseSuccess;
import com.dongkap.common.exceptions.BaseControllerException;
import com.dongkap.common.utils.SuccessCode;
import com.dongkap.dto.common.ApiBaseResponse;
import com.dongkap.dto.notification.WebSubscriptionDto;
import com.dongkap.notification.service.WebPushNotificationImplService;

@RequestMapping("/api/notification")
@RestController
public class WebPushNotificationCtrl extends BaseControllerException {

    @Autowired
    private WebPushNotificationImplService webPushNotificationService;

	@ResponseSuccess(SuccessCode.OK_SCR007)
    @RequestMapping(value = "/push/auth/web/subscribe/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> subscribe(Authentication authentication,
			@RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, required = false) String locale,
    		@RequestBody WebSubscriptionDto subscription) throws Exception {
		webPushNotificationService.subscribe(subscription, authentication.getName());
		return new ResponseEntity<ApiBaseResponse>(new ApiBaseResponse(), SuccessCode.OK_SCR007.getStatus());
    }

}
