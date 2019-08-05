package cai.peter.vision.persistence.repository;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cai.peter.VisionApplication;
import cai.peter.vision.persistence.entity.UserRole;

@SpringBootTest(classes = VisionApplication.class)
@RunWith(SpringRunner.class)
public class UserrolesRepositoryTest {
  /** Logger for this class */
  private static final Logger logger = LoggerFactory.getLogger(UserrolesRepositoryTest.class);

  @Autowired
  UserrolesRepository repo;
  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testFindAll() throws Exception {
    List<UserRole> all = repo.findAll();
    for( UserRole r:all){
      logger.info("testFindAll() - UserRole r2={}", r); //$NON-NLS-1$
    }
  }
}
