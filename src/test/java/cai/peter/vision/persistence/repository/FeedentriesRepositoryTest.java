/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.persistence.repository;

import static org.junit.Assert.*;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.Feed;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = VisionApplication.class)
@RunWith(SpringRunner.class)
public class FeedentriesRepositoryTest {
    @Autowired
    FeedentriesRepository repo;
    @Autowired
    FeedsRepository feedRepo;

    @Test
    public void test() throws Exception {
        Feed feed = feedRepo.findById(7003L).orElseThrow(() -> new Exception(""));
        Long abc = repo.findExisting("abc", feed);
        assertNull(abc);
    }

}