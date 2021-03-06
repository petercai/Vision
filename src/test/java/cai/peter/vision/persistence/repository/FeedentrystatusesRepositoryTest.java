/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.persistence.repository;


import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.FeedSubscription;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.rest.dto.UnreadCount;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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