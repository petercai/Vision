package cai.peter.vision.feed.opml;

import cai.peter.vision.feed.FeedUtils;
import cai.peter.vision.persistence.entity.FeedCategory;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.persistence.repository.FeedcategoriesRepository;
import cai.peter.vision.persistence.service.FeedSubscriptionService;
import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.io.WireFeedInput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.util.List;

@Slf4j
@Component
public class OPMLImporter {

    @Autowired
	private FeedcategoriesRepository feedCategoryDAO;
    @Autowired
	private FeedSubscriptionService feedSubscriptionService;
//	private CacheService cache;

	public void importOpml(User user, String xml) {
		xml = xml.substring(xml.indexOf('<'));
		WireFeedInput input = new WireFeedInput();
		try {
			Opml feed = (Opml) input.build(new StringReader(xml));
			List<Outline> outlines = feed.getOutlines();
			for (int i = 0; i < outlines.size(); i++) {
				handleOutline(user, outlines.get(i), null, i);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	private void handleOutline(User user, Outline outline, FeedCategory parent, int position) {
		List<Outline> children = outline.getChildren();
		if (CollectionUtils.isNotEmpty(children)) {
			String name = FeedUtils.truncate(outline.getText(), 128);
			if (name == null) {
				name = FeedUtils.truncate(outline.getTitle(), 128);
			}
			FeedCategory category = feedCategoryDAO.findByName(user, name, parent);
			if (category == null) {
				if (StringUtils.isBlank(name)) {
					name = "Unnamed category";
				}

				category = new FeedCategory();
				category.setName(name);
				category.setParent(parent);
				category.setUser(user);
				category.setPosition(position);
				feedCategoryDAO.save(category);
			}

			for (int i = 0; i < children.size(); i++) {
				handleOutline(user, children.get(i), category, i);
			}
		} else {
			String name = FeedUtils.truncate(outline.getText(), 128);
			if (name == null) {
				name = FeedUtils.truncate(outline.getTitle(), 128);
			}
			if (StringUtils.isBlank(name)) {
				name = "Unnamed subscription";
			}
			// make sure we continue with the import process even if a feed failed
			try {
				feedSubscriptionService.subscribe(user, outline.getXmlUrl(), name, parent, position);
//			} catch (FeedSubscriptionException e) {
//				throw e;
			} catch (Exception e) {
				log.error("error while importing "+outline.getXmlUrl(), e);
			}
		}
//		cache.invalidateUserRootCategory(user);
	}
}
