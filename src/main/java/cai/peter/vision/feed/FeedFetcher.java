package cai.peter.vision.feed;

import java.io.IOException;
import java.util.Date;


import cai.peter.vision.persistence.entity.Feed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import com.rometools.rome.io.FeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeedFetcher {

	@Autowired
	private FeedParser parser;
	@Autowired
	private HttpGetter getter;

	public FetchedFeed fetch(String feedUrl, boolean extractFeedUrlFromHtml, String lastModified, String eTag, Date lastPublishedDate,
			String lastContentHash) throws FeedException, ClientProtocolException, IOException, HttpGetter.NotModifiedException {
		log.debug("Fetching feed {}", feedUrl);
		FetchedFeed fetchedFeed = null;

		int timeout = 20000;

		HttpGetter.HttpResult result = getter.getBinary(feedUrl, lastModified, eTag, timeout);
		byte[] content = result.getContent();

		try {
			fetchedFeed = parser.parse(result.getUrlAfterRedirect(), content);
		} catch (FeedException e) {
			if (extractFeedUrlFromHtml) {
				String extractedUrl = extractFeedUrl(StringUtils.newStringUtf8(result.getContent()), feedUrl);
				if (org.apache.commons.lang3.StringUtils.isNotBlank(extractedUrl)) {
					feedUrl = extractedUrl;

					result = getter.getBinary(extractedUrl, lastModified, eTag, timeout);
					content = result.getContent();
					fetchedFeed = parser.parse(result.getUrlAfterRedirect(), content);
				} else {
					throw e;
				}
			} else {
				throw e;
			}
		}

		if (content == null) {
			throw new IOException("Feed content is empty.");
		}

		String hash = DigestUtils.sha1Hex(content);
		if (lastContentHash != null && hash != null && lastContentHash.equals(hash)) {
			log.debug("content hash not modified: {}", feedUrl);
			throw new HttpGetter.NotModifiedException("content hash not modified");
		}

		if (lastPublishedDate != null && fetchedFeed.getFeed().getLastPublishedDate() != null
				&& lastPublishedDate.getTime() == fetchedFeed.getFeed().getLastPublishedDate().getTime()) {
			log.debug("publishedDate not modified: {}", feedUrl);
			throw new HttpGetter.NotModifiedException("publishedDate not modified");
		}

		Feed feed = fetchedFeed.getFeed();
		feed.setLastModifiedHeader(result.getLastModifiedSince());
		feed.setEtagHeader(FeedUtils.truncate(result.getETag(), 255));
		feed.setLastContentHash(hash);
		fetchedFeed.setFetchDuration(result.getDuration());
		fetchedFeed.setUrlAfterRedirect(result.getUrlAfterRedirect());
		return fetchedFeed;
	}

	private String extractFeedUrl(String html, String baseUri) {
		String foundUrl = null;

		Document doc = Jsoup.parse(html, baseUri);
		String root = doc.children().get(0).tagName();
		if ("html".equals(root)) {
			Elements atom = doc.select("link[type=application/atom+xml]");
			Elements rss = doc.select("link[type=application/rss+xml]");
			if (!atom.isEmpty()) {
				foundUrl = atom.get(0).attr("abs:href");
			} else if (!rss.isEmpty()) {
				foundUrl = rss.get(0).attr("abs:href");
			}
		}
		return foundUrl;
	}
}
