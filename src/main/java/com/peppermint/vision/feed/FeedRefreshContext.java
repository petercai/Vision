package com.peppermint.vision.feed;

import java.util.List;

import com.peppermint.vision.persistence.entity.Feed;
import com.peppermint.vision.persistence.entity.FeedEntry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedRefreshContext {
	private Feed feed;
	private List<FeedEntry> entries;
	private boolean urgent;

	public FeedRefreshContext(Feed feed, boolean isUrgent) {
		this.feed = feed;
		this.urgent = isUrgent;
	}
}
