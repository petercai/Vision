package cai.peter.vision.feed;

import org.apache.commons.lang.builder.ToStringBuilder;
import de.jayefem.log4e.MethodParameterStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

import cai.peter.vision.persistence.entity.Feed;
import cai.peter.vision.persistence.entity.FeedEntry;
import cai.peter.vision.persistence.entity.FeedEntryContent;
import cai.peter.vision.persistence.entity.FeedSubscription;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.persistence.repository.FeedsubscriptionsRepository;
import cai.peter.vision.persistence.service.FeedUpdateService;
import cai.peter.vision.service.PubSubService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.google.common.util.concurrent.Striped;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FeedRefreshUpdater /*implements Managed*/ {

//	private final SessionFactory sessionFactory;
	private final FeedUpdateService feedUpdateService;
	private final PubSubService pubSubService;
	private final FeedQueues queues;
//	private final CommaFeedConfiguration config;
	private final FeedsubscriptionsRepository feedSubscriptionDAO;
//	private final CacheService cache;


//	private Meter entryCacheMiss;
//	private Meter entryCacheHit;
//	private Meter feedUpdated;
//	private Meter entryInserted;

	public FeedRefreshUpdater(FeedUpdateService feedUpdateService, PubSubService pubSubService,
		FeedQueues queues, FeedsubscriptionsRepository feedSubscriptionDAO) {
		this.feedUpdateService = feedUpdateService;
		this.pubSubService = pubSubService;
		this.queues = queues;
		this.feedSubscriptionDAO = feedSubscriptionDAO;


//		locks = Striped.lazyWeakLock(threads * 100000);

	}



	@Async("feedRefreshUpdaterExecutor")
	public void updateFeed(FeedRefreshContext context) {

		boolean ok = true;
		final Feed feed = context.getFeed();
		List<FeedEntry> entries = context.getEntries();
		if (entries.isEmpty()) {
			feed.setMessage("Feed has no entries");
		} else {
//				List<String> lastEntries = cache.getLastEntries(feed);
//				List<String> currentEntries = new ArrayList<>();

			List<FeedSubscription> subscriptions = Collections.EMPTY_LIST;
			for (FeedEntry entry : entries) {
//					String cacheKey = cache.buildUniqueEntryKey(feed, entry);
				/*if (!lastEntries.contains(cacheKey)) */{
//						log.debug("cache miss for {}", entry.getUrl());
//						if (subscriptions == null) {
//							subscriptions = /*UnitOfWork.call(sessionFactory, () -> */feedSubscriptionDAO.findByFeed(feed);
//						}
					ok &= addEntry(feed, entry, subscriptions);
//						entryCacheMiss.mark();
				} /*else {
						log.debug("cache hit for {}", entry.getUrl());
//						entryCacheHit.mark();
					}*/

//					currentEntries.add(cacheKey);
			}
//				cache.setLastEntries(feed, currentEntries);

			if (subscriptions == null) {
				feed.setMessage("No new entries found");
			} else if (!subscriptions.isEmpty()) {
				List<User> users = subscriptions.stream().map(s -> s.getUser()).collect(Collectors.toList());
//					cache.invalidateUnreadCount(subscriptions.toArray(new FeedSubscription[0]));
//					cache.invalidateUserRootCategory(users.toArray(new User[0]));
			}
		}

		if (false /*config.getApplicationSettings().getPubsubhubbub()*/) {
			handlePubSub(feed);
		}
		if (!ok) {
			// requeue asap
			feed.setDisabledUntil(new Date(0));
		}
//			feedUpdated.mark();
		queues.giveBack(feed);

	}



	private boolean addEntry(final Feed feed, final FeedEntry entry, final List<FeedSubscription> subscriptions) {

		boolean success = false;

		// lock on feed, make sure we are not updating the same feed twice at
		// the same time
//		String key1 = StringUtils.trimToEmpty("" + feed.getId());
//
//		// lock on content, make sure we are not updating the same entry
//		// twice at the same time
//		FeedEntryContent content = entry.getContent();
//		String key2 = DigestUtils.sha1Hex(StringUtils.trimToEmpty(content.getContent() + content.getTitle()));
//
//		Iterator<Lock> iterator = locks.bulkGet(Arrays.asList(key1, key2)).iterator();
//		Lock lock1 = iterator.next();
//		Lock lock2 = iterator.next();
//		boolean locked1 = false;
//		boolean locked2 = false;
//		try
		{
//			locked1 = lock1.tryLock(1, TimeUnit.MINUTES);
//			locked2 = lock2.tryLock(1, TimeUnit.MINUTES);
//			if (locked1 && locked2)
			{
				boolean inserted = /*UnitOfWork.call(sessionFactory, () ->*/ feedUpdateService.addEntry(feed, entry, subscriptions)/*)*/;
//				if (inserted) {
//					entryInserted.mark();
//				}
				success = true;
			}
//			else {
//				log.error("lock timeout for " + feed.getUrl() + " - " + key1);
//			}
		}
    //		catch (InterruptedException e) {
    //			log.error("interrupted while waiting for lock for " + feed.getUrl() + " : " +
    // e.getMessage(), e);
    //		}
    //		finally {
    //			if (locked1) {
    //				lock1.unlock();
    //			}
    //			if (locked2) {
    //				lock2.unlock();
    //			}
    //		}

		return success;
	}

	private void handlePubSub(final Feed feed) {
		if (feed.getPushHub() != null && feed.getPushTopic() != null) {
			Date lastPing = feed.getPushLastPing();
			Date now = new Date();
			if (lastPing == null || lastPing.before(DateUtils.addDays(now, -3))) {
        new Thread() {
          @Override
          public void run() {
            log.info("$Thread.run() - start"); // $NON-NLS-1$

            pubSubService.subscribe(feed);

            log.info("$Thread.run() - end"); // $NON-NLS-1$
          }
        }.start();
			}
		}
	}

}
