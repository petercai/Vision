package com.peppermint.vision.persistence.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peppermint.vision.feed.FeedUtils;
import com.peppermint.vision.persistence.entity.FeedEntryContent;
import com.peppermint.vision.persistence.repository.FeedentrycontentsRepository;

@Service
public class FeedEntryContentService {

    @Autowired
	private  FeedentrycontentsRepository feedEntryContentDAO;

	/**
	 * this is NOT thread-safe
	 */
	public FeedEntryContent findOrCreate(FeedEntryContent content, String baseUrl) {

		String contentHash = DigestUtils.sha1Hex(StringUtils.trimToEmpty(content.getContent()));
		String titleHash = DigestUtils.sha1Hex(StringUtils.trimToEmpty(content.getTitle()));
		Long existingId = feedEntryContentDAO.findExisting(contentHash, titleHash);

		FeedEntryContent result = null;
		if (existingId == null) {
			content.setContentHash(contentHash);
			content.setTitleHash(titleHash);

			content.setAuthor(FeedUtils.truncate(FeedUtils.handleContent(content.getAuthor(), baseUrl, true), 128));
			content.setTitle(FeedUtils.truncate(FeedUtils.handleContent(content.getTitle(), baseUrl, true), 2048));
			content.setContent(FeedUtils.handleContent(content.getContent(), baseUrl, false));
			result = content;
			feedEntryContentDAO.save(result);
		} else {
			result = new FeedEntryContent();
			result.setId(existingId);
		}
		return result;
	}
}
