package cai.peter.vision.persistence.service;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.favicon.AbstractFaviconFetcher.Favicon;
import cai.peter.vision.persistence.entity.Feed;

@SpringBootTest(classes=VisionApplication.class)
@RunWith(SpringRunner.class)
public class FeedServiceTest {

  @Autowired FeedService service;

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testFindOrCreate() throws Exception {
    Feed feed = service.findOrCreate("http://www.infoq.com/cn/feed?token=JompMfRYuikk9igzNwCQqyWczwTzADit");
    assertNotNull(feed);
    Favicon favicon = service.fetchFavicon(feed);
    assertNotNull(favicon);
  }
}
