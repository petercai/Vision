package com.peppermint.vision.rest.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class UserModel implements Serializable {

	
	private Long id;

	
	private String name;

	
	private String email;

	
	private String apiKey;

	
	private String password;

	
	private boolean enabled;

	
	private Date created;

	
	private Date lastLogin;

	
	private boolean admin;

}
