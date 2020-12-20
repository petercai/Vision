package cai.peter.vision.persistence.repository;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.UserRole;
import java.util.List;
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


  @Test
  public void testFindAll() throws Exception {
    List<UserRole> all = repo.findAll();
    for( UserRole r:all){
      logger.info("testFindAll() - UserRole r2={}", r); //$NON-NLS-1$
    }
  }
}
