/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.persistence.dao;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.FeedSubscription;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.persistence.repository.FeedsubscriptionsRepository;
import cai.peter.vision.persistence.repository.UsersRepository;
import cai.peter.vision.rest.dto.UnreadCount;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = VisionApplication.class)
@ExtendWith(SpringExtension.class)
@Slf4j
class SubscriptionDAOTest {

  @Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Autowired SubscriptionDAO subscriptionDAO;
  @Autowired FeedsubscriptionsRepository repo;
  @Autowired UsersRepository userRepo;

  @Test
  void testGetUnreadCount() {
    User peter = userRepo.getAdmin();
    List<UnreadCount> result = subscriptionDAO.getUnreadCount(peter);
    HashMap<Long, FeedSubscription> map =
        repo.findUserAll(peter).stream()
            .collect(HashMap::new, (n, v) -> n.put(v.getFeed().getId(), v), HashMap::putAll);

    result.stream()
        .forEach(
            c -> {
              log.info("Subscrption {} unread count {}",map.get(c.getFeedId()).getTitle(),c.getUnreadCount());
//              log.info(map.get(c.getFeedId()).toString());
            });
  }
}
