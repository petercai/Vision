package com.peppermint.vision.rest.dto;

import java.io.Serializable;
import java.util.Date;

import com.peppermint.vision.feed.FeedUtils;
import com.peppermint.vision.persistence.entity.Feed;
import com.peppermint.vision.persistence.entity.FeedCategory;
import com.peppermint.vision.persistence.entity.FeedSubscription;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Subscription implements Serializable {

	public static Subscription build(FeedSubscription subscription, String publicUrl, UnreadCount unreadCount) {
		Date now = new Date();
		FeedCategory category = subscription.getCategory();
		Feed feed = subscription.getFeed();
		Subscription sub = new Subscription();
		sub.setId(subscription.getId());
		sub.setName(subscription.getTitle());
		sub.setPosition(subscription.getPosition());
		sub.setMessage(feed.getMessage());
		sub.setErrorCount(feed.getErrorCount());
		sub.setFeedUrl(feed.getUrl());
		sub.setFeedLink(feed.getLink());
		sub.setIconUrl(FeedUtils.getFaviconUrl(subscription, publicUrl));
		sub.setLastRefresh(feed.getLastUpdated());
		sub.setNextRefresh((feed.getDisabledUntil() != null && feed.getDisabledUntil().before(now)) ? null : feed.getDisabledUntil());
		sub.setUnread(unreadCount.getUnreadCount());
		sub.setNewestItemTime(unreadCount.getNewestItemTime());
		sub.setCategoryId(category == null ? null : String.valueOf(category.getId()));
		sub.setFilter(subscription.getFilter());
		return sub;
	}

	
	private Long id;

	
	private String name;

	
	private String message;

	
	private int errorCount;

	
	private Date lastRefresh;

	
	private Date nextRefresh;

	
	private String feedUrl;

	
	private String feedLink;

	
	private String iconUrl;

	
	private long unread;

	
	private String categoryId;

	
	private Integer position;

	
	private Date newestItemTime;

	
	private String filter;

}