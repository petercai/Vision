/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.persistence.repository;


import static org.junit.jupiter.api.Assertions.assertNull;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.Feed;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = VisionApplication.class)
@ExtendWith(SpringExtension.class)
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