package com.peppermint.vision.rest.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.peppermint.vision.persistence.entity.AbstractModel;
import com.peppermint.vision.persistence.entity.UserRole.Role;

@Data
public class User implements UserDetails {
  private Long id;
  private String name;
  private String email;
  private String password;
  private String apiKey;
  //	private byte[] salt;
  private boolean disabled;
  private Date lastLogin;
  private Date created;
  //	private String recoverPasswordToken;
  //	private Date recoverPasswordTokenDate;
  //	private Date lastFullRefresh;
  private List<Role> roles;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    return Optional.ofNullable(roles).orElse(Collections.emptyList()).stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return this.name;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return !disabled;
  }
  //	private String recoverPasswordToken;
  //	private Date recoverPasswordTokenDate;
  //	private Date lastFullRefresh;
}
