package cai.peter.vision.persistence.service;

import java.util.List;


import cai.peter.vision.feed.FeedQueues;
import cai.peter.vision.feed.FeedUtils;
import cai.peter.vision.persistence.entity.Feed;
import cai.peter.vision.persistence.entity.FeedCategory;
import cai.peter.vision.persistence.entity.FeedSubscription;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.persistence.repository.FeedsubscriptionsRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


@Service
public class FeedSubscriptionService {

    public FeedSubscriptionService(FeedsubscriptionsRepository feedSubscriptionDAO, FeedService feedService, FeedQueues queues) {
        this.feedSubscriptionDAO = feedSubscriptionDAO;
        this.feedService = feedService;
        this.queues = queues;
    }

    @SuppressWarnings("serial")
	public static class FeedSubscriptionException extends RuntimeException {
		private FeedSubscriptionException(String msg) {
			super(msg);
		}
	}

//	private final FeedEntryStatusDAO feedEntryStatusDAO;
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

		FeedSubscription sub = feedSubscriptionDAO.findByFeed(user, feed);
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



}
