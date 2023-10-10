package cai.peter.vision.service;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.base.MoreObjects;
import com.peppermint.vision.persistence.entity.User;
import com.peppermint.vision.persistence.entity.UserRole;
import com.peppermint.vision.persistence.entity.UserRole.Role;
import com.peppermint.vision.service.UserService;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@SpringBootTest
class UserServiceTest {

  @Autowired
  UserService userService;

  @Test
  void getRoles() {
    User user = userService.getUser("admin");
    Set<UserRole> roles = userService.getRoles(user);
    roles.stream().forEach(role -> log.info(MoreObjects.toStringHelper(role).add("role", role.getRole()).toString()));
  }

  @Test
  void getUser() {
    User admin = userService.getUser("admin");
    log.info(admin.toString());
  }

  @Test
  void loadUserByUsername() {
    UserDetails admin = userService.loadUserByUsername("admin");
    assertNotNull(admin);
    assertNotNull(admin.getPassword());
    log.info(admin.toString());
  }
}