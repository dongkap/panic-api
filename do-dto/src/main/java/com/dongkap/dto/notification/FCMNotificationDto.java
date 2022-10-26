package com.dongkap.dto.notification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FCMNotificationDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5053426923486522719L;
    private String to;
	private String contentAvailable;
    private Map<String, Object> notification = new HashMap<String, Object>();
    private Map<String, Object> data = new HashMap<String, Object>();

}
