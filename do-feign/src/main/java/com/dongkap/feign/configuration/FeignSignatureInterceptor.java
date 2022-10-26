package com.dongkap.feign.configuration;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dongkap.common.security.SignatureEncrypt;
import com.dongkap.common.utils.DateUtil;

@Service
public class FeignSignatureInterceptor {
	
	@Value("${dongkap.signature.private-key}")
	private String privateKey;
	
	@Value("${dongkap.signature.public-key}")
	private String publicKey;
	
	public String getTimestamp() {
		return Double.valueOf(Math.floor(new Date().getTime()/1000)).toString();
	}
	
	public String getPublicKey() {
		return this.publicKey;
	}
	
	public String getSignature(String path, String jwt) {
		String datenow = DateUtil.formatDate(new Date(), DateUtil.DEFAULT_FORMAT_DATE);
		String message = this.getPublicKey() + ":" + 
				this.getTimestamp() + ":" +
				datenow  + ":" +
				path  + ":" +
				jwt;
		try {
			return SignatureEncrypt.getInstance().hash(this.privateKey, message);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
