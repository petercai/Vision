/*
 * Copyright (c) 2020. Peter Cai
 */

package com.peppermint.vision.persistence.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.peppermint.vision.persistence.entity.User;
import com.peppermint.vision.rest.dto.UnreadCount;

@Repository
public class SubscriptionDAO {

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private static final String UNREDA_COUNT =
      "Select"
          + "    FEEDSUBSCRIPTIONS.id,"
          + "    Count(FEEDS.id) As count,"
          + "    FEEDSUBSCRIPTIONS.title,"
          + "    Max(FEEDENTRIES.updated) As latest_update"
          + " From"
          + "    FEEDENTRIES Inner Join"
          + "    FEEDS On FEEDENTRIES.feed_id = FEEDS.id Inner Join"
          + "    FEEDSUBSCRIPTIONS On FEEDSUBSCRIPTIONS.feed_id = FEEDS.id Left Join"
          + "    FEEDENTRYSTATUSES On FEEDENTRYSTATUSES.entry_id = FEEDENTRIES.id"
          + "            And FEEDENTRYSTATUSES.subscription_id = FEEDSUBSCRIPTIONS.id"
          + " Where"
          + "    FEEDSUBSCRIPTIONS.user_id = :id And"
          + "    (FEEDENTRYSTATUSES.read_status = False Or"
          + "        FEEDENTRYSTATUSES.read_status Is Null)"
          + " Group By"
          + "    FEEDSUBSCRIPTIONS.id,"
          + "    FEEDSUBSCRIPTIONS.title"
;

  public List<UnreadCount> getUnreadCount(User user) {
    List<UnreadCount> counts =
        namedParameterJdbcTemplate.query(
            UNREDA_COUNT,
            new BeanPropertySqlParameterSource(user),
            (rs, ronNum) ->
                UnreadCount.builder()
                    .subscriptionId(rs.getLong("id"))
                    .unreadCount(rs.getLong("count"))
                    .newestItemTime(rs.getTimestamp("latest_update"))
                    .build());
    return counts;
  }
}
