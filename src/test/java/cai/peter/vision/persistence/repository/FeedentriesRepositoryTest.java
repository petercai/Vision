/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.persistence.repository;


import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.peppermint.vision.VisionApplication;
import com.peppermint.vision.persistence.entity.Feed;
import com.peppermint.vision.persistence.repository.FeedentriesRepository;
import com.peppermint.vision.persistence.repository.FeedsRepository;

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