package cai.peter.vision.rest.api;

import cai.peter.vision.common.VisionConfiguration;
import cai.peter.vision.persistence.dao.SubscriptionDAO;
import cai.peter.vision.persistence.entity.FeedCategory;
import cai.peter.vision.persistence.entity.FeedSubscription;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.persistence.repository.FeedcategoriesRepository;
import cai.peter.vision.persistence.repository.FeedentrystatusesRepository;
import cai.peter.vision.persistence.repository.FeedsubscriptionsRepository;
import cai.peter.vision.persistence.service.FeedEntryService;
import cai.peter.vision.persistence.service.FeedSubscriptionService;
import cai.peter.vision.rest.dto.Category;
import cai.peter.vision.rest.dto.Subscription;
import cai.peter.vision.rest.dto.UnreadCount;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/category")
public class CategoryController {

  public static final String ALL = "all";
  public static final String STARRED = "starred";

  private final FeedcategoriesRepository feedCategoryDAO;
  private final FeedentrystatusesRepository feedEntryStatusDAO;
  private final FeedsubscriptionsRepository feedSubscriptionDAO;
  private final FeedEntryService feedEntryService;
  private final FeedSubscriptionService feedSubscriptionService;
  //	private final CacheService cache;
  @Autowired private /*final*/ VisionConfiguration config;

  //	@GetMapping("/entries")
  //	@ApiOperation(value = "Get category entries", notes = "Get a list of category entries",
  // response = Entries.class)
  //	public ResponseEntity<Entries> getCategoryEntries(/*@SecurityCheck*/ User user,
  //		@RequestParam("id") String id,
  //		@RequestParam("readType") ReadingMode readType,
  //		@RequestParam("newerThan") Long newerThan,
  //		@RequestParam(name = "offset", defaultValue = "0") int offset,
  //		@RequestParam(name = "limit", defaultValue = "20") int limit,
  //		@RequestParam(value = "order", defaultValue = "desc") UserSettings.ReadingOrder order,
  //		@RequestParam("keywords") String keywords,
  //		@RequestParam(name = "onlyIds", defaultValue = "false") boolean onlyIds,
  //		@RequestParam("excludedSubscriptionIds") String excludedSubscriptionIds,
  //		@RequestParam("tag") String tag) {
  //
  //		Preconditions.checkNotNull(readType);
  //
  //		keywords = StringUtils.trimToNull(keywords);
  //		Preconditions.checkArgument(keywords == null || StringUtils.length(keywords) >= 3);
  //		List<FeedEntryKeyword> entryKeywords = FeedEntryKeyword.fromQueryString(keywords);
  //
  //		limit = Math.min(limit, 1000);
  //		limit = Math.max(0, limit);
  //
  //		Entries entries = new Entries();
  //		entries.setOffset(offset);
  //		entries.setLimit(limit);
  //		boolean unreadOnly = readType == ReadingMode.unread;
  //		if (StringUtils.isBlank(id)) {
  //			id = ALL;
  //		}
  //
  //		Date newerThanDate = newerThan == null ? null : new Date(newerThan);
  //
  //		List<Long> excludedIds = null;
  //		if (StringUtils.isNotEmpty(excludedSubscriptionIds)) {
  //			excludedIds =
  // Arrays.stream(excludedSubscriptionIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
  //		}
  //
  //		if (ALL.equals(id)) {
  //			entries.setName(Optional.ofNullable(tag).orElse("All"));
  //			List<FeedSubscription> subs = feedSubscriptionDAO.findUserAll(user);
  //			removeExcludedSubscriptions(subs, excludedIds);
  //			List<FeedEntryStatus> list = feedEntryStatusDAO.findBySubscriptions(user, subs, unreadOnly,
  // entryKeywords, newerThanDate,
  //					offset, limit + 1, order, true, onlyIds, tag);
  //
  //			for (FeedEntryStatus status : list) {
  //				entries.getEntries().add(Entry.build(status, config.getApplicationSettings().getPublicUrl(),
  //						config.getApplicationSettings().getImageProxyEnabled()));
  //			}
  //
  //		} else if (STARRED.equals(id)) {
  //			entries.setName("Starred");
  //			List<FeedEntryStatus> starred = feedEntryStatusDAO.findStarred(user, newerThanDate, offset,
  // limit + 1, order, !onlyIds);
  //			for (FeedEntryStatus status : starred) {
  //				entries.getEntries().add(Entry.build(status, config.getApplicationSettings().getPublicUrl(),
  //						config.getApplicationSettings().getImageProxyEnabled()));
  //			}
  //		} else {
  //			FeedCategory parent = feedCategoryDAO.findById(user, Long.valueOf(id));
  //			if (parent != null) {
  //				List<FeedCategory> categories = feedCategoryDAO.findAllChildrenCategories(user, parent);
  //				List<FeedSubscription> subs = feedSubscriptionDAO.findByCategories(user, categories);
  //				removeExcludedSubscriptions(subs, excludedIds);
  //				List<FeedEntryStatus> list = feedEntryStatusDAO.findBySubscriptions(user, subs, unreadOnly,
  // entryKeywords, newerThanDate,
  //						offset, limit + 1, order, true, onlyIds, tag);
  //
  //				for (FeedEntryStatus status : list) {
  //					entries.getEntries().add(Entry.build(status,
  // config.getApplicationSettings().getPublicUrl(),
  //							config.getApplicationSettings().getImageProxyEnabled()));
  //				}
  //				entries.setName(parent.getName());
  //			} else {
  //				return Response.status(Status.NOT_FOUND).entity("<message>category not
  // found</message>").build();
  //			}
  //		}
  //
  //		boolean hasMore = entries.getEntries().size() > limit;
  //		if (hasMore) {
  //			entries.setHasMore(true);
  //			entries.getEntries().remove(entries.getEntries().size() - 1);
  //		}
  //
  //		entries.setTimestamp(System.currentTimeMillis());
  //		entries.setIgnoredReadStatus(STARRED.equals(id) || keywords != null || tag != null);
  //		FeedUtils.removeUnwantedFromSearch(entries.getEntries(), entryKeywords);
  //		return Response.ok(entries).build();
  //	}

  //	@Path("/entriesAsFeed")
  //	@GET
  //	@UnitOfWork
  //	@ApiOperation(value = "Get category entries as feed", notes = "Get a feed of category entries")
  //	@Produces(MediaType.APPLICATION_XML)
  //	@Timed
  //	public Response getCategoryEntriesAsFeed(/*@SecurityCheck(apiKeyAllowed = true)*/ User user,
  //			 @RequestParam("id") String id,
  //			@ApiParam(
  //					value = "all entries or only unread ones",
  //					allowableValues = "all,unread",
  //					required = true) @DefaultValue("all") @RequestParam("readType") ReadingMode readType,
  //			 @RequestParam("newerThan") Long newerThan,
  //			 @DefaultValue("0") @RequestParam("offset") int offset,
  //			 @DefaultValue("20") @RequestParam("limit") int limit,
  //			 @RequestParam("order") @DefaultValue("desc") ReadingOrder order,
  //			@ApiParam(
  //					value = "search for keywords in either the title or the content of the entries, separated
  // by spaces, 3 characters minimum") @RequestParam("keywords") String keywords,
  //			 @DefaultValue("false") @RequestParam("onlyIds") boolean onlyIds,
  //			@ApiParam(
  //					value = "comma-separated list of excluded subscription ids")
  // @RequestParam("excludedSubscriptionIds") String excludedSubscriptionIds,
  //			 @RequestParam("tag") String tag) {
  //
  //		Response response = getCategoryEntries(user, id, readType, newerThan, offset, limit, order,
  // keywords, onlyIds,
  //				excludedSubscriptionIds, tag);
  //		if (response.getStatus() != Status.OK.getStatusCode()) {
  //			return response;
  //		}
  //		Entries entries = (Entries) response.getEntity();
  //
  //		SyndFeed feed = new SyndFeedImpl();
  //		feed.setFeedType("rss_2.0");
  //		feed.setTitle("CommaFeed - " + entries.getName());
  //		feed.setDescription("CommaFeed - " + entries.getName());
  //		feed.setLink(config.getApplicationSettings().getPublicUrl());
  //		feed.setEntries(entries.getEntries().stream().map(e ->
  // e.asRss()).collect(Collectors.toList()));
  //
  //		SyndFeedOutput output = new SyndFeedOutput();
  //		StringWriter writer = new StringWriter();
  //		try {
  //			output.output(feed, writer);
  //		} catch (Exception e) {
  //			writer.write("Could not get feed information");
  //			log.error(e.getMessage(), e);
  //		}
  //		return Response.ok(writer.toString()).build();
  //	}

  //	@Path("/mark")
  //	@POST
  //	@UnitOfWork
  //	@ApiOperation(value = "Mark category entries", notes = "Mark feed entries of this category as
  // read")
  //	@Timed
  //	public Response markCategoryEntries(/*@SecurityCheck*/ User user,
  //			 MarkRequest req) {
  //		Preconditions.checkNotNull(req);
  //		Preconditions.checkNotNull(req.getId());
  //
  //		Date olderThan = req.getOlderThan() == null ? null : new Date(req.getOlderThan());
  //		String keywords = req.getKeywords();
  //		List<FeedEntryKeyword> entryKeywords = FeedEntryKeyword.fromQueryString(keywords);
  //
  //		if (ALL.equals(req.getId())) {
  //			List<FeedSubscription> subs = feedSubscriptionDAO.findAll(user);
  //			removeExcludedSubscriptions(subs, req.getExcludedSubscriptions());
  //			feedEntryService.markSubscriptionEntries(user, subs, olderThan, entryKeywords);
  //		} else if (STARRED.equals(req.getId())) {
  //			feedEntryService.markStarredEntries(user, olderThan);
  //		} else {
  //			FeedCategory parent = feedCategoryDAO.findById(user, Long.valueOf(req.getId()));
  //			List<FeedCategory> categories = feedCategoryDAO.findAllChildrenCategories(user, parent);
  //			List<FeedSubscription> subs = feedSubscriptionDAO.findByCategories(user, categories);
  //			removeExcludedSubscriptions(subs, req.getExcludedSubscriptions());
  //			feedEntryService.markSubscriptionEntries(user, subs, olderThan, entryKeywords);
  //		}
  //		return Response.ok().build();
  //	}

  private void removeExcludedSubscriptions(List<FeedSubscription> subs, List<Long> excludedIds) {
    if (CollectionUtils.isNotEmpty(excludedIds)) {
      Iterator<FeedSubscription> it = subs.iterator();
      while (it.hasNext()) {
        FeedSubscription sub = it.next();
        if (excludedIds.contains(sub.getId())) {
          it.remove();
        }
      }
    }
  }

  //	@Path("/add")
  //	@POST
  //	@UnitOfWork
  //	@ApiOperation(value = "Add a category", notes = "Add a new feed category", response =
  // Long.class)
  //	@Timed
  //	public Response addCategory(/*@SecurityCheck*/ User user,  AddCategoryRequest req) {
  //		Preconditions.checkNotNull(req);
  //		Preconditions.checkNotNull(req.getName());
  //
  //		FeedCategory cat = new FeedCategory();
  //		cat.setName(req.getName());
  //		cat.setUser(user);
  //		cat.setPosition(0);
  //		String parentId = req.getParentId();
  //		if (parentId != null && !ALL.equals(parentId)) {
  //			FeedCategory parent = new FeedCategory();
  //			parent.setId(Long.valueOf(parentId));
  //			cat.setParent(parent);
  //		}
  //		feedCategoryDAO.saveOrUpdate(cat);
  //		cache.invalidateUserRootCategory(user);
  //		return Response.ok(cat.getId()).build();
  //	}

  //	@POST
  //	@Path("/delete")
  //	@UnitOfWork
  //	@ApiOperation(value = "Delete a category", notes = "Delete an existing feed category")
  //	@Timed
  //	public Response deleteCategory(/*@SecurityCheck*/ User user,  IDRequest req) {
  //
  //		Preconditions.checkNotNull(req);
  //		Preconditions.checkNotNull(req.getId());
  //
  //		FeedCategory cat = feedCategoryDAO.findById(user, req.getId());
  //		if (cat != null) {
  //			List<FeedSubscription> subs = feedSubscriptionDAO.findByCategory(user, cat);
  //			for (FeedSubscription sub : subs) {
  //				sub.setCategory(null);
  //			}
  //			feedSubscriptionDAO.saveOrUpdate(subs);
  //			List<FeedCategory> categories = feedCategoryDAO.findAllChildrenCategories(user, cat);
  //			for (FeedCategory child : categories) {
  //				if (!child.getId().equals(cat.getId()) && child.getParent().getId().equals(cat.getId())) {
  //					child.setParent(null);
  //				}
  //			}
  //			feedCategoryDAO.saveOrUpdate(categories);
  //
  //			feedCategoryDAO.delete(cat);
  //			cache.invalidateUserRootCategory(user);
  //			return Response.ok().build();
  //		} else {
  //			return Response.status(Status.NOT_FOUND).build();
  //		}
  //	}

  //	@POST
  //	@Path("/modify")
  //	@UnitOfWork
  //	@ApiOperation(value = "Rename a category", notes = "Rename an existing feed category")
  //	@Timed
  //	public Response modifyCategory(/*@SecurityCheck*/ User user,  CategoryModificationRequest req)
  // {
  //		Preconditions.checkNotNull(req);
  //		Preconditions.checkNotNull(req.getId());
  //
  //		FeedCategory category = feedCategoryDAO.findById(user, req.getId());
  //
  //		if (StringUtils.isNotBlank(req.getName())) {
  //			category.setName(req.getName());
  //		}
  //
  //		FeedCategory parent = null;
  //		if (req.getParentId() != null && !CategoryController.ALL.equals(req.getParentId())
  //				&& !StringUtils.equals(req.getParentId(), String.valueOf(req.getId()))) {
  //			parent = feedCategoryDAO.findById(user, Long.valueOf(req.getParentId()));
  //		}
  //		category.setParent(parent);
  //
  //		if (req.getPosition() != null) {
  //			List<FeedCategory> categories = feedCategoryDAO.findByParent(user, parent);
  //			Collections.sort(categories, new Comparator<FeedCategory>() {
  //				@Override
  //				public int compare(FeedCategory o1, FeedCategory o2) {
  //					return ObjectUtils.compare(o1.getPosition(), o2.getPosition());
  //				}
  //			});
  //
  //			int existingIndex = -1;
  //			for (int i = 0; i < categories.size(); i++) {
  //				if (Objects.equals(categories.get(i).getId(), category.getId())) {
  //					existingIndex = i;
  //				}
  //			}
  //			if (existingIndex != -1) {
  //				categories.remove(existingIndex);
  //			}
  //
  //			categories.add(Math.min(req.getPosition(), categories.size()), category);
  //			for (int i = 0; i < categories.size(); i++) {
  //				categories.get(i).setPosition(i);
  //			}
  //			feedCategoryDAO.saveOrUpdate(categories);
  //		} else {
  //			feedCategoryDAO.saveOrUpdate(category);
  //		}
  //
  //		feedCategoryDAO.saveOrUpdate(category);
  //		cache.invalidateUserRootCategory(user);
  //		return Response.ok().build();
  //	}

  //	@POST
  //	@Path("/collapse")
  //	@UnitOfWork
  //	@ApiOperation(value = "Collapse a category", notes = "Save collapsed or expanded status for a
  // category")
  //	@Timed
  //	public Response collapse(/*@SecurityCheck*/ User user,  CollapseRequest req) {
  //		Preconditions.checkNotNull(req);
  //		Preconditions.checkNotNull(req.getId());
  //
  //		FeedCategory category = feedCategoryDAO.findById(user, req.getId());
  //		if (category == null) {
  //			return Response.status(Status.NOT_FOUND).build();
  //		}
  //		category.setCollapsed(req.isCollapse());
  //		feedCategoryDAO.saveOrUpdate(category);
  //		cache.invalidateUserRootCategory(user);
  //		return Response.ok().build();
  //	}

  //	@GET
  //	@Path("/unreadCount")
  //	@UnitOfWork
  //	@ApiOperation(value = "Get unread count for feed subscriptions", response = UnreadCount.class,
  // responseContainer = "List")
  //	@Timed
  //	public Response getUnreadCount(/*@SecurityCheck*/ User user) {
  //		Map<Long, UnreadCount> unreadCount = feedSubscriptionService.getUnreadCount(user);
  //		return Response.ok(Lists.newArrayList(unreadCount.values())).build();
  //	}

  @GetMapping("/get")
  //	@ApiOperation(value = "Get feed categories", notes = "Get all categories and subscriptions of
  // the user", response = Category.class)
  public ResponseEntity<Category> getSubscriptions(/*@SecurityCheck User user*/) {
    Category root = null /*cache.getUserRootCategory(user)*/;
    User user = new User();
    user.setId(1000L);
    if (root == null) {
      log.debug("tree cache miss for {}", user.getId());
      List<FeedCategory> categories = feedCategoryDAO.getByUser(user);
      List<FeedSubscription> subscriptions = feedSubscriptionDAO.getByUser(user);
      SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
      List<UnreadCount> unreadCount = subscriptionDAO.getUnreadCount(user);
      //			Map<Long, UnreadCount> unreadCount = feedSubscriptionService.getUnreadCount(user);

      root =
          buildCategory(
              null,
              categories,
              subscriptions,
              unreadCount.stream()
                  .collect(Collectors.toMap(UnreadCount::getFeedId, Function.identity())));
      root.setId("all");
      root.setName("All");
      //			cache.setUserRootCategory(user, root);
    }
    return new ResponseEntity<>(root, HttpStatus.OK);
  }

  private Category buildCategory(
      Long id,
      List<FeedCategory> categories,
      List<FeedSubscription> subscriptions,
      Map<Long, UnreadCount> unreadCount) {
    Category category = new Category();
    category.setId(String.valueOf(id));
    category.setExpanded(true);

    for (FeedCategory c : categories) {
      if ((id == null && c.getParent() == null)
          || (c.getParent() != null && Objects.equals(c.getParent().getId(), id))) {
        Category child = buildCategory(c.getId(), categories, subscriptions, unreadCount);
        child.setId(String.valueOf(c.getId()));
        child.setName(c.getName());
        child.setPosition(c.getPosition());
        if (c.getParent() != null && c.getParent().getId() != null) {
          child.setParentId(String.valueOf(c.getParent().getId()));
        }
        child.setExpanded(!c.isCollapsed());
        category.getChildren().add(child);
      }
    }
    Collections.sort(
        category.getChildren(),
        new Comparator<Category>() {
          @Override
          public int compare(Category o1, Category o2) {
            return ObjectUtils.compare(o1.getPosition(), o2.getPosition());
          }
        });

    for (FeedSubscription subscription : subscriptions) {
      if ((id == null && subscription.getCategory() == null)
          || (subscription.getCategory() != null
              && Objects.equals(subscription.getCategory().getId(), id))) {
        UnreadCount uc = unreadCount.get(subscription.getId());
        Subscription sub =
            Subscription.build(subscription, config.getApplicationSettings().getPublicUrl(), uc);
        category.getFeeds().add(sub);
      }
    }
    Collections.sort(
        category.getFeeds(),
        new Comparator<Subscription>() {
          @Override
          public int compare(Subscription o1, Subscription o2) {
            return ObjectUtils.compare(o1.getPosition(), o2.getPosition());
          }
        });
    return category;
  }
}
