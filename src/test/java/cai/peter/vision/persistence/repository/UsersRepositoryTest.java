package cai.peter.vision.persistence.repository;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.service.PasswordEncryptionService;
import cai.peter.vision.persistence.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes = VisionApplication.class)
@RunWith(SpringRunner.class)
public class UsersRepositoryTest {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(UsersRepositoryTest.class);

    @Autowired
    UsersRepository repo;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

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
