package cai.peter.vision.persistence.repository;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.service.PasswordEncryptionService;
import com.google.common.base.MoreObjects;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = VisionApplication.class)
@ExtendWith(SpringExtension.class)
public class UsersRepositoryTest {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(UsersRepositoryTest.class);

    @Autowired
    UsersRepository repo;


    @Test
    public void testFindAll() throws Exception {
        List<User> all = repo.findAll();
        for (User u : all) {
            logger.info("testFindAll() - User={}",
//              ToStringBuilder.reflectionToString(u, ToStringStyle.JSON_STYLE)
              MoreObjects.toStringHelper(u)
                .add("id", u.getId())
                .add("name", u.getName())
                .add("email", u.getEmail())
                .toString()
            ); //$NON-NLS-1$
        }
    }

    @Test
    public void testGetAdmin() {
        User admin = repo.getUser("admin");
        logger.info("testGetAdmin() - User admin={}", MoreObjects.toStringHelper(admin)
          .add("id", admin.getId())
          .add("name", admin.getName())
          .add("email", admin.getEmail())
          .toString()); // $NON-NLS-1$

        assertNotNull(admin);

    }

    @Test
    public void testFindByName() {
        User admin = repo.findByName("admin@commafeed.com");
        logger.info("testGetAdmin() - User admin={}", MoreObjects.toStringHelper(admin)
          .add("id", admin.getId())
          .add("name", admin.getName())
          .add("email", admin.getEmail())
          .toString()); // $NON-NLS-1$

        assertNotNull(admin);

    }

    @Test
    @Disabled
    public void testCreateAdmin(){
        User admin = new User();
        admin.setName("admin");
        admin.setEmail("caipeter@outlook.com");
        byte[] salt = PasswordEncryptionService.generateSalt();
        admin.setSalt(salt);
        admin.setPassword("".getBytes());
        repo.save(admin);
    }
}
