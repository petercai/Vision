/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.feed.opml;

import cai.peter.vision.persistence.entity.FeedCategory;
import cai.peter.vision.persistence.entity.FeedSubscription;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.persistence.repository.FeedcategoriesRepository;
import cai.peter.vision.persistence.repository.FeedsubscriptionsRepository;
import com.google.common.base.MoreObjects;
import com.rometools.opml.feed.opml.Attribute;
import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OPMLExporter {

    @Autowired
	private FeedcategoriesRepository feedCategoryDAO;
    @Autowired
	private FeedsubscriptionsRepository feedSubscriptionDAO;

	public Opml export(User user) {
		Opml opml = new Opml();
		opml.setFeedType("opml_1.1");
		opml.setTitle(String.format("%s subscriptions in Vision", user.getName()));
		opml.setCreated(new Date());

		List<FeedCategory> categories = feedCategoryDAO.getByUser(user);
		Collections.sort(categories,
				(e1, e2) -> MoreObjects.firstNonNull(e1.getPosition(), 0) - MoreObjects.firstNonNull(e2.getPosition(), 0));

		List<FeedSubscription> subscriptions = feedSubscriptionDAO.getByUser(user);
		Collections.sort(subscriptions,
				(e1, e2) -> MoreObjects.firstNonNull(e1.getPosition(), 0) - MoreObjects.firstNonNull(e2.getPosition(), 0));

		// export root categories
		for (FeedCategory cat : categories.stream().filter(c -> c.getParent() == null).collect(Collectors.toList())) {
			opml.getOutlines().add(buildCategoryOutline(cat, categories, subscriptions));
		}

		// export root subscriptions
		for (FeedSubscription sub : subscriptions.stream().filter(s -> s.getCategory() == null).collect(Collectors.toList())) {
			opml.getOutlines().add(buildSubscriptionOutline(sub));
		}

		return opml;

	}

	private Outline buildCategoryOutline(FeedCategory cat, List<FeedCategory> categories, List<FeedSubscription> subscriptions) {
		Outline outline = new Outline();
		outline.setText(cat.getName());
		outline.setTitle(cat.getName());

		for (FeedCategory child : categories.stream().filter(c -> c.getParent() != null && c.getParent().getId().equals(cat.getId()))
				.collect(Collectors.toList())) {
			outline.getChildren().add(buildCategoryOutline(child, categories, subscriptions));
		}

		for (FeedSubscription sub : subscriptions.stream()
				.filter(s -> s.getCategory() != null && s.getCategory().getId().equals(cat.getId())).collect(Collectors.toList())) {
			outline.getChildren().add(buildSubscriptionOutline(sub));
		}
		return outline;
	}

	private Outline buildSubscriptionOutline(FeedSubscription sub) {
		Outline outline = new Outline();
		outline.setText(sub.getTitle());
		outline.setTitle(sub.getTitle());
		outline.setType("rss");
		outline.getAttributes().add(new Attribute("xmlUrl", sub.getFeed().getUrl()));
		if (sub.getFeed().getLink() != null) {
			outline.getAttributes().add(new Attribute("htmlUrl", sub.getFeed().getLink()));
		}
		return outline;
	}
}
