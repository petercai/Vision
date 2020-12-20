package cai.peter.vision.persistence.service;







import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.favicon.AbstractFaviconFetcher.Favicon;
import cai.peter.vision.persistence.entity.Feed;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest(classes=VisionApplication.class)
@ExtendWith(SpringExtension.class)
public class FeedServiceTest {

  @Autowired FeedService service;

  @Test
  public void testFindOrCreate() throws Exception {
    Feed feed = service.findOrCreate("http://www.infoq.com/cn/feed?token=JompMfRYuikk9igzNwCQqyWczwTzADit");
    assertNotNull(feed);
    Favicon favicon = service.fetchFavicon(feed);
    assertNotNull(favicon);
  }
}
