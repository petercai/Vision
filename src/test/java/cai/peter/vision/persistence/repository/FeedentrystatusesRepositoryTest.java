/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.persistence.repository;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.peppermint.vision.VisionApplication;
import com.peppermint.vision.persistence.entity.FeedSubscription;
import com.peppermint.vision.persistence.entity.User;
import com.peppermint.vision.persistence.repository.FeedentrystatusesRepository;
import com.peppermint.vision.persistence.repository.FeedsubscriptionsRepository;
import com.peppermint.vision.persistence.repository.UsersRepository;
import com.peppermint.vision.rest.dto.UnreadCount;

@Slf4j
@SpringBootTest(classes = VisionApplication.class)
@ExtendWith(SpringExtension.class)
public class FeedentrystatusesRepositoryTest {

    @Autowired
    private FeedentrystatusesRepository repo;
    @Autowired
    private FeedsubscriptionsRepository subRepo;
    @Autowired
    private UsersRepository userRepo;
    @Test
    public void getReadCount() {
        User admin = userRepo.getAdmin();
        FeedSubscription sub = subRepo.findByUser(admin, 1008l);
        UnreadCount readCount = repo.getReadCount(admin, sub);
        log.info("{} read account {}", sub.getTitle(), readCount);

    }

//    @Test
//    public void testGetUnreadCount(){
//        User admin = userRepo.getAdmin();
//        List<UnreadCount> unreadCountAll = repo.getUnreadCountAll(admin);
//        unreadCountAll.stream().forEach(c->log.info(c.toString()));
//    }

}