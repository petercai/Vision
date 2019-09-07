package cai.peter.vision.feed;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;


import cai.peter.vision.persistence.entity.Feed;
import cai.peter.vision.persistence.repository.FeedsRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


//@Singleton
@Component
public class FeedQueues {

    @Autowired
	private  FeedsRepository feedDAO;
//	private final CommaFeedConfiguration config;

	private Queue<FeedRefreshContext> addQueue = new ConcurrentLinkedQueue<>();
	private Queue<FeedRefreshContext> takeQueue = new ConcurrentLinkedQueue<>();
	private Queue<Feed> giveBackQueue = new ConcurrentLinkedQueue<>();



	/**
	 * take a feed from the refresh queue
	 */
	public synchronized FeedRefreshContext take() {
		FeedRefreshContext context = takeQueue.poll();

		if (context == null) {
			refill();
			context = takeQueue.poll();
		}
		return context;
	}

	/**
	 * add a feed to the refresh queue
	 */
	public void add(Feed feed, boolean urgent) {
		int refreshInterval = 15;//config.getApplicationSettings().getRefreshIntervalMinutes();
//		if (feed.getLastUpdated() == null || feed.getLastUpdated().before(DateUtils.addMinutes(new Date(), -1 * refreshInterval)))
		{
			boolean alreadyQueued = addQueue.stream().anyMatch(c -> c.getFeed().getId().equals(feed.getId()));
			if (!alreadyQueued) {
				addQueue.add(new FeedRefreshContext(feed, urgent));
			}
		}
	}

	/**
	 * refills the refresh queue and empties the giveBack queue while at it
	 */
	private void refill() {

		List<FeedRefreshContext> contexts = new ArrayList<>();
		int batchSize = Math.min(100, 3 * 20/*config.getApplicationSettings().getBackgroundThreads()*/);

		// add feeds we got from the add() method
		int addQueueSize = addQueue.size();
		for (int i = 0; i < Math.min(batchSize, addQueueSize); i++) {
			contexts.add(addQueue.poll());
		}

		// add feeds that are up to refresh from the database
        // TODO: support in future
//		int count = batchSize - contexts.size();
//		if (count > 0) {
//			List<Feed> feeds = /*UnitOfWork.call(sessionFactory, () ->*/ feedDAO.findNextUpdatable(count, getLastLoginThreshold())/*)*/;
//			List<Feed> feeds =  feedDAO.findNextUpdatable(count, getLastLoginThreshold());
//			for (Feed feed : feeds) {
//				contexts.add(new FeedRefreshContext(feed, false));
//			}
//		}

		// set the disabledDate as we use it in feedDAO to decide what to refresh next. We also use a map to remove
		// duplicates.
		Map<Long, FeedRefreshContext> map = new LinkedHashMap<>();
		for (FeedRefreshContext context : contexts) {
			Feed feed = context.getFeed();
			feed.setDisabledUntil(DateUtils.addMinutes(new Date(), 15/*config.getApplicationSettings().getRefreshIntervalMinutes()*/));
			map.put(feed.getId(), context);
		}

		// refill the queue
		takeQueue.addAll(map.values());

		// add feeds from the giveBack queue to the map, overriding duplicates
		int giveBackQueueSize = giveBackQueue.size();
		for (int i = 0; i < giveBackQueueSize; i++) {
			Feed feed = giveBackQueue.poll();
			map.put(feed.getId(), new FeedRefreshContext(feed, false));
		}

		// update all feeds in the database
		map.values().stream().map(c -> c.getFeed()).forEach(f->feedDAO.save(f));
//		/*UnitOfWork.run(sessionFactory, () -> */feedDAO.saveOrUpdate(feeds)/*)*/;
	}

	/**
	 * give a feed back, updating it to the database during the next refill()
	 */
	public void giveBack(Feed feed) {
		String normalized = FeedUtils.normalizeURL(feed.getUrl());
		feed.setNormalizedUrl(normalized);
		feed.setNormalizedUrlHash(DigestUtils.sha1Hex(normalized));
		feed.setLastUpdated(new Date());
		giveBackQueue.add(feed);
	}

	private Date getLastLoginThreshold() {
//		if (config.getApplicationSettings().getHeavyLoad()) {
//			return DateUtils.addDays(new Date(), -30);
//		} else {
			return null;
//		}
	}

	public int isAllDone(){
	    return addQueue.size() + giveBackQueue.size() + takeQueue.size();
    }


}
