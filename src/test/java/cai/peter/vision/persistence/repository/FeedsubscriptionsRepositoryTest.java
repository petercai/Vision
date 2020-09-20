package cai.peter.vision.persistence.repository;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.Feed;
import cai.peter.vision.persistence.entity.FeedSubscription;
import cai.peter.vision.persistence.entity.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
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
import org.springframework.util.function.SingletonSupplier;

import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes = VisionApplication.class)
@RunWith(SpringRunner.class)
public class FeedsubscriptionsRepositoryTest {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(FeedsubscriptionsRepositoryTest.class);

    /**
     * Logger for this class
     */
    @Autowired
    FeedsubscriptionsRepository repo;
    @Autowired
    UsersRepository userRepo;
    @Autowired
    FeedsRepository feedRepo;

    /**
     * Logger for this class
     */

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        List<FeedSubscription> subs = repo.findAll();

        for (FeedSubscription sub : subs) {
            LOGGER.info("test() - FeedSubscription=" + sub); // $NON-NLS-1$

        }

    }

    @Test
    public void findByUserAndId() {
        User admin = userRepo.getAdmin();
        FeedSubscription sub = repo.findByUser(admin, 7003L);

        LOGGER.info("findByUserAndId() - FeedSubscription sub={}", sub); // $NON-NLS-1$
        assertNotNull(sub);
    }

    @Test
    public void findByFeed() throws Exception {


        Feed feed = feedRepo.findById(7003L).orElseThrow(()->new Exception(""));
        List<FeedSubscription> subs = repo.findByFeed(feed);

        LOGGER.info("findByUserAndId() - FeedSubscription sub={}", subs); // $NON-NLS-1$
        assertNotNull(subs);
    }

    @Test
    public void findUserAll() {
        User admin = userRepo.getAdmin();

        List<FeedSubscription> subs = repo.findUserAll(admin);
        for (FeedSubscription sub : subs) {
            LOGGER.info("findUserAll() - FeedSubscription sub={}", sub); // $NON-NLS-1$

        }
    }
}