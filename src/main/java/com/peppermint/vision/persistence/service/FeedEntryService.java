package com.peppermint.vision.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peppermint.vision.persistence.entity.FeedEntryStatus;
import com.peppermint.vision.persistence.entity.FeedSubscription;
import com.peppermint.vision.persistence.entity.User;
import com.peppermint.vision.persistence.repository.FeedentriesRepository;
import com.peppermint.vision.persistence.repository.FeedentrystatusesRepository;
import com.peppermint.vision.persistence.repository.FeedsubscriptionsRepository;

import java.util.Date;
import java.util.List;

@Service
public class FeedEntryService {

    @Autowired
    private FeedsubscriptionsRepository feedSubscriptionDAO;
    @Autowired
    private FeedentriesRepository feedEntryDAO;
    @Autowired
    private FeedentrystatusesRepository feedEntryStatusDAO;
//	private final CacheService cache;

    public void markEntry(User user, Long entryId, boolean read) {

        feedEntryDAO.findById(entryId).ifPresent(entry -> {
            FeedSubscription sub = feedSubscriptionDAO.findByFeedPerUser(user, entry.getFeed());
            if (sub == null) {
                return;
            }

            FeedEntryStatus status = feedEntryStatusDAO.getByUserAndSubscriptionAndEntry(user, sub, entry);
            if (status.isMarkable()) {
                status.setRead(read);
                feedEntryStatusDAO.save(status);
            }
        });


    }

    public void starEntry(User user, Long entryId, Long subscriptionId, boolean starred) {

        FeedSubscription sub = feedSubscriptionDAO.findByUser(user, subscriptionId);
        if (sub == null) {
            return;
        }

        feedEntryDAO.findById(entryId).ifPresent(entry -> {
            FeedEntryStatus status = feedEntryStatusDAO.getByUserAndSubscriptionAndEntry(user, sub, entry);
            status.setStarred(starred);
            feedEntryStatusDAO.save(status);
        });

    }

//    public void markSubscriptionEntries(User user, List<FeedSubscription> subscriptions, Date olderThan, List<FeedEntryKeyword> keywords) {
//        List<FeedEntryStatus> statuses = feedEntryStatusDAO.findBySubscriptions(user, subscriptions, true, keywords, null, -1, -1, null,
//                false, false, null);
//        markList(statuses, olderThan);
//        cache.invalidateUnreadCount(subscriptions.toArray(new FeedSubscription[0]));
//        cache.invalidateUserRootCategory(user);
//    }

    public void markStarredEntries(User user, Date olderThan) {
        List<FeedEntryStatus> statuses = feedEntryStatusDAO.findStarred(user);
        markList(statuses, olderThan);
    }

    private void markList(List<FeedEntryStatus> statuses, Date olderThan) {
        for (FeedEntryStatus status : statuses) {
            if (!status.isRead()) {
                Date inserted = status.getEntry().getInserted();
                if (olderThan == null || inserted == null || olderThan.after(inserted)) {
                    status.setRead(true);
                    feedEntryStatusDAO.save(status);
                }
            }
        }
    }
}
