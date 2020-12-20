package cai.peter.vision.persistence.repository;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.service.PasswordEncryptionService;
import java.util.List;
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
            User u2 = u;
            logger.info("testFindAll() - User u2={}", u2); //$NON-NLS-1$
        }
    }

    @Test
    public void testGetAdmin() {
        User admin = repo.getAdmin();
        logger.info("testGetAdmin() - User admin={}", admin); // $NON-NLS-1$

        assertNotNull(admin);

    }

    @Test
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
