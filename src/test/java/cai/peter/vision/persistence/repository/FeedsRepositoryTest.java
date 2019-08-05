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

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.Feed;

@SpringBootTest(classes=VisionApplication.class)
@RunWith(SpringRunner.class)
public class FeedsRepositoryTest {
  /** Logger for this class */
  private static final Logger logger = LoggerFactory.getLogger(FeedsRepositoryTest.class);

	@Autowired
	FeedsRepository repo;
  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testFindByUrl() throws Exception {
    Feed feed = repo.findByUrl("http://www.techradar.com/rss");
    logger.info("testFindByUrl() - Feed feed={}", feed); //$NON-NLS-1$
  }

  @Test
  public void testFindAll() throws Exception {
    List<Feed> findAll = repo.findAll();
    for( Feed f: findAll) {
      logger.info("testFindAll() - Feed f2={}", f); //$NON-NLS-1$
    }
  }
}
