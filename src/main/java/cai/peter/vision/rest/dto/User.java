package cai.peter.vision.rest.dto;

import cai.peter.vision.persistence.entity.AbstractModel;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
public class User implements UserDetails {
	private Long id;
	private String name;
	private String email;
	private byte[] password;
		private String apiKey;
//	private byte[] salt;
	private boolean disabled;
	private Date lastLogin;
	private Date created;
	//	private String recoverPasswordToken;
//	private Date recoverPasswordTokenDate;
//	private Date lastFullRefresh;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return !disabled;
	}
//	private String recoverPasswordToken;
//	private Date recoverPasswordTokenDate;
//	private Date lastFullRefresh;
}
