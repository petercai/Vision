/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.persistence.service;

import java.util.Arrays;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.peppermint.vision.VisionApplication;
import com.peppermint.vision.feed.FeedQueues;
import com.peppermint.vision.persistence.entity.Feed;
import com.peppermint.vision.persistence.entity.FeedCategory;
import com.peppermint.vision.persistence.entity.FeedSubscription;
import com.peppermint.vision.persistence.entity.User;
import com.peppermint.vision.persistence.repository.FeedentrystatusesRepository;
import com.peppermint.vision.persistence.repository.FeedsubscriptionsRepository;
import com.peppermint.vision.persistence.repository.UsersRepository;
import com.peppermint.vision.persistence.service.FeedService;
import com.peppermint.vision.persistence.service.FeedSubscriptionService;
import com.peppermint.vision.rest.dto.UnreadCount;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = VisionApplication.class)
@ExtendWith(SpringExtension.class)
@Slf4j
class FeedSubscriptionServiceTest {
  @Mock FeedentrystatusesRepository feedEntryStatusDAO;
  @Mock FeedsubscriptionsRepository feedSubscriptionDAO;
  @Mock FeedService feedService;
  @Mock FeedQueues queues;

  @Autowired UsersRepository usrRepo;

  @Autowired FeedSubscriptionService feedSubscriptionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testSubscribe() {
    when(feedSubscriptionDAO.findByFeedPerUser(any(), any())).thenReturn(new FeedSubscription());
    when(feedService.findOrCreate(anyString())).thenReturn(new Feed());

    Feed result = feedSubscriptionService.subscribe(new User(), "url", "title");
    Assertions.assertEquals(new Feed(), result);
  }

  @Test
  void testSubscribe2() {
    when(feedSubscriptionDAO.findByFeedPerUser(any(), any())).thenReturn(new FeedSubscription());
    when(feedService.findOrCreate(anyString())).thenReturn(new Feed());

    Feed result = feedSubscriptionService.subscribe(new User(), "url", "title", new FeedCategory());
    Assertions.assertEquals(new Feed(), result);
  }

  @Test
  void testSubscribe3() {
    when(feedSubscriptionDAO.findByFeedPerUser(any(), any())).thenReturn(new FeedSubscription());
    when(feedService.findOrCreate(anyString())).thenReturn(new Feed());

    Feed result =
        feedSubscriptionService.subscribe(new User(), "url", "title", new FeedCategory(), 0);
    Assertions.assertEquals(new Feed(), result);
  }

  @Test
  void testUnsubscribe() {
    when(feedSubscriptionDAO.findByUser(any(), anyLong())).thenReturn(new FeedSubscription());

    boolean result = feedSubscriptionService.unsubscribe(new User(), Long.valueOf(1));
    Assertions.assertEquals(true, result);
  }

  @Test
  void testRefreshAll() {
    when(feedSubscriptionDAO.findUserAll(any()))
        .thenReturn(Arrays.<FeedSubscription>asList(new FeedSubscription()));

    feedSubscriptionService.refreshAll(new User());
  }

  @Test
  void testGetUnreadCount() {
    User peter = usrRepo.getUser("peter");
    Map<Long, UnreadCount> result = feedSubscriptionService.getUnreadCount(peter);
    log.info(result.toString());
  }
}

