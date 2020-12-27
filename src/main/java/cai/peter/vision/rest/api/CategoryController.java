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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
  private final SubscriptionDAO subscriptionDAO;

  //	private final CacheService cache;
  @Autowired private /*final*/ VisionConfiguration config;





  /**
   * The SecurityContext and SecurityContextHolder are two fundamental classes of Spring Security.
   * The SecurityContext is used to store the details of the currently authenticated user, also
   * known as a principle. So, if you have to get the username or any other user details, you need
   * to get the SecurityContext first.
   *
   * <p>The SecurityContextHolder is a helper class that provides access to the security context. By
   * default, it uses a ThreadLocal object to store security context, which means that the security
   * context is always available to methods in the same thread of execution, even if you don't pass
   * the SecurityContext object around
   *
   * <p>In order to get the current username, you first need a SecurityContext, which is obtained
   * from the SecurityContextHolder. This SecurityContext kept the user details in an Authentication
   * object, which can be obtained by calling the getAuthentication() method.
   *
   * <p>Once you got the Authentication object, you can either cast it into UserDetails or use it as
   * it is. The UserDetails object is the one that Spring Security uses to keep user-related
   * information.
   *
   * <p>Once you got the Authentication object, you can either cast it into UserDetails or use it as
   * it is. The UserDetails object is the one that Spring Security uses to keep user-related
   * information.
   *
   * @return
   */
  @GetMapping("/get")
  public ResponseEntity<Category> getSubscriptions(/*@SecurityCheck User user*/ ) {


    Category root = null /*cache.getUserRootCategory(user)*/;
    User user = new User();
    user.setId(1000L);
    if (root == null) {
      log.debug("tree cache miss for {}", user.getId());
      List<FeedCategory> categories = feedCategoryDAO.getByUser(user);
      List<FeedSubscription> subscriptions = feedSubscriptionDAO.getByUser(user);

      List<UnreadCount> unreadCount = subscriptionDAO.getUnreadCount(user);
      //			Map<Long, UnreadCount> unreadCount = feedSubscriptionService.getUnreadCount(user);

      root =
          buildCategory(
              null,
              categories,
              subscriptions,
              unreadCount.stream()
                  .collect(Collectors.toMap(UnreadCount::getSubscriptionId, Function.identity())));
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
        UnreadCount uc =
            Optional.ofNullable(unreadCount.get(subscription.getId()))
                .orElse(
                    UnreadCount.builder()
                        .subscriptionId(subscription.getId())
                        .unreadCount(0)
                        .build());
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
