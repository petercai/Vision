package cai.peter.vision.service;

import cai.peter.vision.common.VisionConfiguration;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.persistence.entity.UserRole;
import cai.peter.vision.persistence.entity.UserRole.Role;
import cai.peter.vision.persistence.repository.FeedcategoriesRepository;
import cai.peter.vision.persistence.repository.FeedsubscriptionsRepository;
import cai.peter.vision.persistence.repository.UserrolesRepository;
import cai.peter.vision.persistence.repository.UsersRepository;
import cai.peter.vision.persistence.repository.UsersettingsRepository;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final FeedcategoriesRepository feedCategoryDAO;
  private final FeedsubscriptionsRepository feedSubscriptionDAO;
  private final UsersRepository userDAO;
  private final UserrolesRepository userRoleDAO;
  private final UsersettingsRepository userSettingsDAO;

  private final PasswordEncryptionService encryptionService;
  private final VisionConfiguration config;

  //	private final PostLoginActivities postLoginActivities;

  /** try to log in with given credentials */
  public Optional<User> login(String nameOrEmail, String password) {
    if (nameOrEmail == null || password == null) {
      return Optional.empty();
    }

    User user = userDAO.findByName(nameOrEmail);
    //		if (user == null) {
    //			user = userDAO.findByEmail(nameOrEmail);
    //		}
    if (user != null && !user.isDisabled()) {
      boolean authenticated =
          encryptionService.authenticate(password, user.getPassword(), user.getSalt());
      if (authenticated) {
        performPostLoginActivities(user);
        return Optional.of(user);
      }
    }
    return Optional.empty();
  }

  /** try to log in with given api key */
  public Optional<User> login(String apiKey) {
    if (apiKey == null) {
      return Optional.empty();
    }

    //		User user = userDAO.findByApiKey(apiKey);
    //		if (user != null && !user.isDisabled()) {
    //			performPostLoginActivities(user);
    //			return Optional.of(user);
    //		}
    return Optional.empty();
  }

  /** should triggers after successful login */
  public void performPostLoginActivities(User user) {
    //		postLoginActivities.executeFor(user);
  }

  public User register(String name, String password, String email, Collection<Role> roles) {
    return register(name, password, email, roles, false);
  }

  public User register(
      String name,
      String password,
      String email,
      Collection<Role> roles,
      boolean forceRegistration) {

    Preconditions.checkNotNull(name);
    Preconditions.checkArgument(
        StringUtils.length(name) <= 32, "Name too long (32 characters maximum)");
    Preconditions.checkNotNull(password);

    if (!forceRegistration) {
      Preconditions.checkState(
          config.getApplicationSettings().getAllowRegistrations(),
          "Registrations are closed on this CommaFeed instance");

      Preconditions.checkNotNull(email);
      Preconditions.checkArgument(
          StringUtils.length(name) >= 3, "Name too short (3 characters minimum)");
      Preconditions.checkArgument(
          forceRegistration || StringUtils.length(password) >= 6,
          "Password too short (6 characters maximum)");
      Preconditions.checkArgument(StringUtils.contains(email, "@"), "Invalid email address");
    }

    Preconditions.checkArgument(userDAO.findByName(name) == null, "Name already taken");
    //		if (StringUtils.isNotBlank(email)) {
    //			Preconditions.checkArgument(userDAO.findByEmail(email) == null, "Email already taken");
    //		}

    User user = new User();
    byte[] salt = encryptionService.generateSalt();
    user.setName(name);
    user.setEmail(email);
    user.setCreated(new Date());
    user.setSalt(salt);
    user.setPassword(encryptionService.getEncryptedPassword(password, salt));
    userDAO.save(user);
    for (Role role : roles) {
      userRoleDAO.save(new UserRole(user, role));
    }
    return user;
  }

  public void unregister(User user) {
    //		feedCategoryDAO.delete(feedCategoryDAO.findAll(user));
    //		userSettingsDAO.delete(userSettingsDAO.findByUser(user));
    //		userRoleDAO.delete(userRoleDAO.findRoles(user.getId()));
    //		feedSubscriptionDAO.delete(feedSubscriptionDAO.findAll(user));
    //		userDAO.delete(user);
  }

  public String generateApiKey(User user) {
    byte[] key =
        encryptionService.getEncryptedPassword(UUID.randomUUID().toString(), user.getSalt());
    return DigestUtils.sha1Hex(key);
  }

  public Set<UserRole> getRoles(User user) {
    return userRoleDAO.findRoles(user.getId());
  }

  public User getUser(String userName) {
    return userDAO.getUser(userName);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User userEntity = userDAO.getUser(username);
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.addConverter(
        new AbstractConverter<byte[], String>() {
          @Override
          protected String convert(byte[] source) {
            return new String(source);
          }
        });
    cai.peter.vision.rest.dto.User user =
        modelMapper.map(userEntity, cai.peter.vision.rest.dto.User.class);
    user.setRoles(
        getRoles(userEntity).stream().map(UserRole::getRole).collect(Collectors.toList()));
    return user;
  }
}
