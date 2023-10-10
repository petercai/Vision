package cai.peter.vision.persistence.service;







import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import com.peppermint.vision.VisionApplication;
import com.peppermint.vision.favicon.AbstractFaviconFetcher.Favicon;
import com.peppermint.vision.persistence.entity.Feed;
import com.peppermint.vision.persistence.service.FeedService;

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
