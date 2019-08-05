package cai.peter.vision.persistence.repository;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.FeedCategory;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VisionApplication.class)
//@DataJpaTest
public class FeedcategoriesRepositoryTest /*implements FeedcategoriesRepository*/ {
  /** Logger for this class */
  private static final Logger logger = LoggerFactory.getLogger(FeedcategoriesRepositoryTest.class);

  @Autowired FeedcategoriesRepository feedcategoriesRepository;

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testFindAll() throws Exception {

    List<FeedCategory> all = feedcategoriesRepository.findAll();
    for(FeedCategory cat : all) {
      logger.info("testFindAll() - FeedCategory c={}", cat); //$NON-NLS-1$
    }
  }
}
