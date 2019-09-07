package cai.peter.vision.feed;

import cai.peter.vision.persistence.entity.Feed;
import cai.peter.vision.persistence.entity.FeedEntry;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FetchedFeed {

	private Feed feed = new Feed();
	private List<FeedEntry> entries = new ArrayList<>();

	private String title;
	private String urlAfterRedirect;
	private long fetchDuration;

}
