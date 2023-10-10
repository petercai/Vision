package com.peppermint.vision.rest.dto;

import com.peppermint.vision.feed.FeedUtils;
import com.peppermint.vision.persistence.entity.FeedEntry;
import com.peppermint.vision.persistence.entity.FeedEntryContent;
import com.peppermint.vision.persistence.entity.FeedEntryStatus;
import com.peppermint.vision.persistence.entity.FeedSubscription;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEnclosureImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
@Data
public class Entry implements Serializable {

	public static Entry build(FeedEntryStatus status, String publicUrl, boolean proxyImages) {
		Entry entry = new Entry();

		FeedEntry feedEntry = status.getEntry();
		FeedSubscription sub = status.getSubscription();
		FeedEntryContent content = feedEntry.getContent();

		entry.setId(String.valueOf(feedEntry.getId()));
		entry.setGuid(feedEntry.getGuid());
		entry.setRead(status.isRead());
		entry.setStarred(status.isStarred());
		entry.setMarkable(status.isMarkable());
		entry.setDate(feedEntry.getUpdated());
		entry.setInsertedDate(feedEntry.getInserted());
		entry.setUrl(feedEntry.getUrl());
		entry.setFeedName(sub.getTitle());
		entry.setFeedId(String.valueOf(sub.getId()));
		entry.setFeedUrl(sub.getFeed().getUrl());
		entry.setFeedLink(sub.getFeed().getLink());
		entry.setIconUrl(FeedUtils.getFaviconUrl(sub, publicUrl));
		entry.setTags(status.getTags().stream().map(t -> t.getName()).collect(Collectors.toList()));

		if (content != null) {
			entry.setRtl(FeedUtils.isRTL(feedEntry));
			entry.setTitle(content.getTitle());
			entry.setContent(proxyImages ? FeedUtils.proxyImages(content.getContent(), publicUrl) : content.getContent());
			entry.setAuthor(content.getAuthor());
			entry.setEnclosureType(content.getEnclosureType());
			entry.setEnclosureUrl(proxyImages && StringUtils.contains(content.getEnclosureType(), "image")
					? FeedUtils.proxyImage(content.getEnclosureUrl(), publicUrl) : content.getEnclosureUrl());
			entry.setCategories(content.getCategories());
		}

		return entry;
	}

	public SyndEntry asRss() {
		SyndEntry entry = new SyndEntryImpl();

		entry.setUri(getGuid());
		entry.setTitle(getTitle());
		entry.setAuthor(getAuthor());

		SyndContentImpl content = new SyndContentImpl();
		content.setValue(getContent());
		entry.setContents(Arrays.<SyndContent> asList(content));

		if (getEnclosureUrl() != null) {
			SyndEnclosureImpl enclosure = new SyndEnclosureImpl();
			enclosure.setType(getEnclosureType());
			enclosure.setUrl(getEnclosureUrl());
			entry.setEnclosures(Arrays.<SyndEnclosure> asList(enclosure));
		}

		entry.setLink(getUrl());
		entry.setPublishedDate(getDate());
		return entry;
	}

	
	private String id;

	
	private String guid;

	
	private String title;

	
	private String content;

	
	private String categories;

	
	private boolean rtl;

	
	private String author;

	
	private String enclosureUrl;

	
	private String enclosureType;

	
	private Date date;

	
	private Date insertedDate;

	
	private String feedId;

	
	private String feedName;

	
	private String feedUrl;

	
	private String feedLink;

	
	private String iconUrl;

	
	private String url;

	
	private boolean read;

	
	private boolean starred;

	private boolean markable;

	
	private List<String> tags;
}
