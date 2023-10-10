package com.peppermint.vision.persistence.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.peppermint.vision.feed.FeedQueues;
import com.peppermint.vision.feed.FeedUtils;
import com.peppermint.vision.persistence.entity.Feed;
import com.peppermint.vision.persistence.entity.FeedCategory;
import com.peppermint.vision.persistence.entity.FeedSubscription;
import com.peppermint.vision.persistence.entity.User;
import com.peppermint.vision.persistence.repository.FeedentrystatusesRepository;
import com.peppermint.vision.persistence.repository.FeedsubscriptionsRepository;
import com.peppermint.vision.rest.dto.UnreadCount;

@Service
@RequiredArgsConstructor
public class FeedSubscriptionService {

	@SuppressWarnings("serial")
	public static class FeedSubscriptionException extends RuntimeException {
		private FeedSubscriptionException(String msg) {
			super(msg);
		}
	}

	private final FeedentrystatusesRepository feedEntryStatusDAO;
	private final FeedsubscriptionsRepository feedSubscriptionDAO;
	private final FeedService feedService;
	private final FeedQueues queues;
//	private final CacheService cache;
//	private final CommaFeedConfiguration config;

	public Feed subscribe(User user, String url, String title) {
		return subscribe(user, url, title, null, 0);
	}

	public Feed subscribe(User user, String url, String title, FeedCategory parent) {
		return subscribe(user, url, title, parent, 0);
	}

	public Feed subscribe(User user, String url, String title, FeedCategory category, int position) {

//		final String pubUrl = config.getApplicationSettings().getPublicUrl();
//		if (StringUtils.isBlank(pubUrl)) {
//			throw new FeedSubscriptionException("Public URL of this Vision instance is not set");
//		}
//		if (url.startsWith(pubUrl)) {
//			throw new FeedSubscriptionException("Could not subscribe to a feed from this Vision instance");
//		}

		Feed feed = feedService.findOrCreate(url);

		FeedSubscription sub = feedSubscriptionDAO.findByFeedPerUser(user, feed);
		if (sub == null) {
			sub = new FeedSubscription();
			sub.setFeed(feed);
			sub.setUser(user);
		}
		sub.setCategory(category);
		sub.setPosition(position);
		sub.setTitle(FeedUtils.truncate(title, 128));
		feedSubscriptionDAO.save(sub);

		queues.add(feed, false);
//		cache.invalidateUserRootCategory(user);
		return feed;
	}

	public boolean unsubscribe(User user, Long subId) {
		FeedSubscription sub = feedSubscriptionDAO.findByUser(user, subId);
		if (sub != null) {
			feedSubscriptionDAO.delete(sub);
//			cache.invalidateUserRootCategory(user);
			return true;
		} else {
			return false;
		}
	}

	public void refreshAll(User user) {
		List<FeedSubscription> subs = feedSubscriptionDAO.findUserAll(user);
		for (FeedSubscription sub : subs) {
			Feed feed = sub.getFeed();
			queues.add(feed, true);
		}
	}

  public Map<Long, UnreadCount> getUnreadCount(User user) {
    return feedSubscriptionDAO.getByUser(user).stream()
        .collect(
            HashMap::new, (o, v) -> o.put(v.getId(), getUnreadCount(user, v)), HashMap::putAll);
  }

  private UnreadCount getUnreadCount(User user, FeedSubscription sub) {
  	UnreadCount count = null/*cache.getUnreadCount(sub)*/;
  	if (count == null) {
//  		log.debug("unread count cache miss for {}", Models.getId(sub));
  		count = feedEntryStatusDAO.getUnreadCount(user, sub);
//  		cache.setUnreadCount(sub, count);
  	}
  	return count;
  }



}
