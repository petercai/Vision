package com.peppermint.vision.feed;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.peppermint.vision.persistence.entity.Feed;
import com.peppermint.vision.persistence.entity.FeedEntry;

@Getter
@Setter
public class FetchedFeed {

	private Feed feed = new Feed();
	private List<FeedEntry> entries = new ArrayList<>();

	private String title;
	private String urlAfterRedirect;
	private long fetchDuration;

}
