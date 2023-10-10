package com.peppermint.vision.persistence.service;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import com.peppermint.vision.favicon.AbstractFaviconFetcher;
import com.peppermint.vision.favicon.AbstractFaviconFetcher.Favicon;
import com.peppermint.vision.feed.FeedUtils;
import com.peppermint.vision.persistence.entity.Feed;
import com.peppermint.vision.persistence.repository.FeedsRepository;

@Service
@Transactional
public class FeedService {


  private final FeedsRepository feedDAO;
  private final Set<AbstractFaviconFetcher> faviconFetchers;

  private Favicon defaultFavicon;

  public FeedService(FeedsRepository feedDAO, Set<AbstractFaviconFetcher> faviconFetchers) {
    		this.feedDAO = feedDAO;
    this.faviconFetchers = faviconFetchers;

    try {
      defaultFavicon =
          new Favicon(
              IOUtils.toByteArray(getClass().getResource("/images/default_favicon.gif")),
              "image/gif");
    } catch (IOException e) {
      throw new RuntimeException("could not load default favicon", e);
    }
  }

  public Feed findOrCreate(String url) {
    String normalized = FeedUtils.normalizeURL(url);
    Feed feed = feedDAO.findByUrl(normalized);
    		if (feed == null) {
    			feed = new Feed();
    			feed.setUrl(url);
    			feed.setNormalizedUrl(normalized);
    			feed.setNormalizedUrlHash(DigestUtils.sha1Hex(normalized));
    			feed.setDisabledUntil(new Date(0));
    			feedDAO.save(feed);
    		}
    return feed;
  }

  public Favicon fetchFavicon(Feed feed) {

    Favicon icon = null;
    for (AbstractFaviconFetcher faviconFetcher : faviconFetchers) {
      icon = faviconFetcher.fetch(feed);
      if (icon != null) {
        break;
      }
    }
    if (icon == null) {
      icon = defaultFavicon;
    }
    return icon;
  }
}
