/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.feed;


import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.Feed;
import cai.peter.vision.persistence.repository.FeedsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = {VisionApplication.class})
@ExtendWith(SpringExtension.class)
public class FeedRefreshWorkerTest {

    @Autowired
    FeedsRepository feedRepo;

    @Autowired
    FeedRefreshWorker workder;

    @Autowired
    FeedQueues queue;


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