/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.feed;

import static org.junit.Assert.*;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.Feed;
import cai.peter.vision.persistence.repository.FeedsRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {VisionApplication.class})
@RunWith(SpringRunner.class)
public class FeedRefreshWorkerTest {

    @Autowired
    FeedsRepository feedRepo;

    @Autowired
    FeedRefreshWorker workder;

    @Autowired
    FeedQueues queue;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void updateFeed() {
    }

    @Test
    public void update() throws Exception {
        long id = 7003L;
        Feed feed = feedRepo.findById(id).orElseThrow(() -> new Exception("Cannot find Feed ID="+id));
        FeedRefreshContext context = new FeedRefreshContext(feed, false);
        workder.update(context);

        while( queue.isAllDone() > 0 )
        {
            Thread.sleep(100L );
        }
            Thread.sleep(5000L );
    }
}