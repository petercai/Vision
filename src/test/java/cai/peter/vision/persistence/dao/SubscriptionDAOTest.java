/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.persistence.dao;

import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.peppermint.vision.VisionApplication;
import com.peppermint.vision.persistence.dao.SubscriptionDAO;
import com.peppermint.vision.persistence.entity.FeedSubscription;
import com.peppermint.vision.persistence.entity.User;
import com.peppermint.vision.persistence.repository.FeedsubscriptionsRepository;
import com.peppermint.vision.persistence.repository.UsersRepository;
import com.peppermint.vision.rest.dto.UnreadCount;

@SpringBootTest(classes = VisionApplication.class)
@ExtendWith(SpringExtension.class)
@Slf4j
class SubscriptionDAOTest {

  @Autowired SubscriptionDAO subscriptionDAO;
  @Autowired FeedsubscriptionsRepository repo;
  @Autowired UsersRepository userRepo;

  @Test
  void testGetUnreadCount() {
    User peter = userRepo.getAdmin();
    List<UnreadCount> result = subscriptionDAO.getUnreadCount(peter);
    HashMap<Long, FeedSubscription> map =
        repo.findUserAll(peter).stream()
            .collect(HashMap::new, (n, v) -> n.put(v.getId(), v), HashMap::putAll);

    result.stream()
        .forEach(
            c -> {
              log.info("Subscrption {} ==> {}",map.get(c.getSubscriptionId()).getTitle(),c.getUnreadCount());
//              log.info(map.get(c.getFeedId()).toString());
            });
  }
}
