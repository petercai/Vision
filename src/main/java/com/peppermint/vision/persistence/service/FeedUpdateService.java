package com.peppermint.vision.persistence.service;

import org.apache.commons.lang.builder.ToStringBuilder;
import de.jayefem.log4e.MethodParameterStyle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import com.peppermint.vision.persistence.entity.Feed;
import com.peppermint.vision.persistence.entity.FeedEntry;
import com.peppermint.vision.persistence.entity.FeedEntryContent;
import com.peppermint.vision.persistence.entity.FeedEntryStatus;
import com.peppermint.vision.persistence.entity.FeedSubscription;
import com.peppermint.vision.persistence.repository.FeedentriesRepository;
import com.peppermint.vision.persistence.repository.FeedentrystatusesRepository;

@Slf4j
@Service
public class FeedUpdateService {

    private final FeedentriesRepository feedEntryDAO;
    private final FeedentrystatusesRepository feedEntryStatusDAO;
    private final FeedEntryContentService feedEntryContentService;
    private final FeedEntryFilteringService feedEntryFilteringService;

	public FeedUpdateService(
		FeedentriesRepository feedEntryDAO, FeedentrystatusesRepository feedEntryStatusDAO,
		FeedEntryContentService feedEntryContentService,
		FeedEntryFilteringService feedEntryFilteringService) {
		this.feedEntryDAO = feedEntryDAO;
		this.feedEntryStatusDAO = feedEntryStatusDAO;
		this.feedEntryContentService = feedEntryContentService;
		this.feedEntryFilteringService = feedEntryFilteringService;
	}

	/**
	 * this is NOT thread-safe
	 */
	public boolean addEntry(Feed feed, FeedEntry entry, List<FeedSubscription> subscriptions) {
    log.info(
        "addEntry(feed={}, entry={}, subscriptions={}) - start",
        feed,
        entry,
        subscriptions); //$NON-NLS-1$

		Long existing = feedEntryDAO.findExisting(entry.getGuid(), feed);
		if (existing != null) {
      log.info("addEntry() - end"); // $NON-NLS-1$
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

    log.info("addEntry() - end"); // $NON-NLS-1$
		return true;
	}
}
