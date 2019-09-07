package cai.peter.vision.persistence.service;

import cai.peter.vision.persistence.entity.Feed;
import cai.peter.vision.persistence.entity.FeedEntry;
import cai.peter.vision.persistence.entity.FeedEntryContent;
import cai.peter.vision.persistence.entity.FeedEntryStatus;
import cai.peter.vision.persistence.entity.FeedSubscription;
import cai.peter.vision.persistence.repository.FeedentriesRepository;
import cai.peter.vision.persistence.repository.FeedentrystatusesRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FeedUpdateService {

    @Autowired
	private FeedentriesRepository feedEntryDAO;
	private FeedentrystatusesRepository feedEntryStatusDAO;
	private FeedEntryContentService feedEntryContentService;
	private FeedEntryFilteringService feedEntryFilteringService;

	/**
	 * this is NOT thread-safe
	 */
	public boolean addEntry(Feed feed, FeedEntry entry, List<FeedSubscription> subscriptions) {

		Long existing = feedEntryDAO.findExisting(entry.getGuid(), feed);
		if (existing != null) {
			return false;
		}

		FeedEntryContent content = feedEntryContentService.findOrCreate(entry.getContent(), feed.getLink());
		entry.setGuidHash(DigestUtils.sha1Hex(entry.getGuid()));
		entry.setContent(content);
		entry.setInserted(new Date());
		entry.setFeed(feed);
		feedEntryDAO.save(entry);

		// if filter does not match the entry, mark it as read
		for (FeedSubscription sub : subscriptions) {
			boolean matches = true;
			try {
				matches = feedEntryFilteringService.filterMatchesEntry(sub.getFilter(), entry);
			} catch (FeedEntryFilteringService.FeedEntryFilterException e) {
				log.error("could not evaluate filter {}", sub.getFilter(), e);
			}
			if (!matches) {
				FeedEntryStatus status = new FeedEntryStatus(sub.getUser(), sub, entry);
				status.setRead(true);
				feedEntryStatusDAO.save(status);
			}
		}

		return true;
	}
}
