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
import cai.peter.vision.persistence.entity.FeedEntryContent;

@SpringBootTest(classes = VisionApplication.class)
@RunWith(SpringRunner.class)
public class FeedentrycontentsRepositoryTest {
    /**
     * Logger for this class
     */
    private static final Logger logger =
            LoggerFactory.getLogger(FeedentrycontentsRepositoryTest.class);

    @Autowired
    FeedentrycontentsRepository repo;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFindAll() throws Exception {
        List<FeedEntryContent> all = repo.findAll();
        for (FeedEntryContent t : all) {
            logger.info("testFindAll() - FeedEntryContent t2={}", t.getContent()); //$NON-NLS-1$
        }
    }
}
