package cai.peter.vision.persistence.repository;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.persistence.entity.UserRole;
import cai.peter.vision.persistence.entity.UserRole.Role;
import com.google.common.base.MoreObjects;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = VisionApplication.class)
@ExtendWith(SpringExtension.class)
public class UserrolesRepositoryTest {
  /** Logger for this class */
  private static final Logger logger = LoggerFactory.getLogger(UserrolesRepositoryTest.class);

  @Autowired
  UserrolesRepository repo;

  @Autowired
  UsersRepository usersRepository;

  @Test
  public void testFindAll() throws Exception {
    List<UserRole> all = repo.findAll();
    for( UserRole r:all){
      logger.info("testFindAll() - UserRole r2={}", r); //$NON-NLS-1$
    }
  }

  @Test
  public void testUserRoles() {
    Set<UserRole> roles = repo.findRoles(1000L);
    roles.stream().forEach(role -> {logger.info(MoreObjects.toStringHelper(role).add("role", role.getRole()).toString());});
  }

  @Test
  public void addAllRoles(){
    User cai = usersRepository.getUser("cai");
    Set<UserRole> roles = repo.findRoles(cai.getId());
    if (roles == null || roles.isEmpty()) {
      Arrays.stream(Role.values()).forEach(role -> {
        UserRole ur = new UserRole();
        ur.setRole(role);
        ur.setUser(cai);
        repo.save(ur);
      });
    }
  }
}
