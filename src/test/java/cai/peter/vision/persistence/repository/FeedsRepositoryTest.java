package cai.peter.vision.persistence.repository;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.peppermint.vision.VisionApplication;
import com.peppermint.vision.persistence.entity.Feed;
import com.peppermint.vision.persistence.repository.FeedsRepository;

@SpringBootTest(classes=VisionApplication.class)
@ExtendWith(SpringExtension.class)
public class FeedsRepositoryTest {
  /** Logger for this class */
  private static final Logger logger = LoggerFactory.getLogger(FeedsRepositoryTest.class);

	@Autowired
	FeedsRepository repo;

  @Test
  public void testFindByUrl() throws Exception {
//    Feed feed = repo.findByRawUrl("http://www.infoq.com/cn/feed?token=JompMfRYuikk9igzNwCQqyWczwTzADit");
    Feed feed = repo.findByRawUrl("http://www.techradar.com/rss");
    logger.info("testFindByUrl() - Feed feed={}", feed); //$NON-NLS-1$
    logger.info("testFindByUrl() - Feed feed.getUrlAfterRedirect={}", feed.getUrlAfterRedirect()); //$NON-NLS-1$
  }

  @Test
  public void testFindAll() throws Exception {
    List<Feed> findAll = repo.findAll();
    for( Feed f: findAll) {
      logger.info("testFindAll() - Feed f2={}", f); //$NON-NLS-1$
    }
  }
}
