package cai.peter.vision.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.Feed;
import cai.peter.vision.persistence.entity.FeedSubscription;
import cai.peter.vision.persistence.entity.User;
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