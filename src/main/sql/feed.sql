SELECT   `FEEDSUBSCRIPTIONS`.`title`,
         `FEEDS`.`link`,
         `FEEDS`.`url` AS `feed_url`,
         `FEEDENTRIES`.`url` AS `news_url`,
         `FEEDENTRYCONTENTS`.`title`,
         `FEEDENTRYCONTENTS`.`content`,
         `FEEDENTRYSTATUSES`.`read_status`,
         `FEEDENTRYSTATUSES`.`starred`
FROM     `FEEDSUBSCRIPTIONS` 
INNER JOIN `FEEDS`  ON `FEEDSUBSCRIPTIONS`.`feed_id` = `FEEDS`.`id` 
INNER JOIN `FEEDENTRIES`  ON `FEEDENTRIES`.`feed_id` = `FEEDS`.`id` 
INNER JOIN `FEEDENTRYCONTENTS`  ON `FEEDENTRIES`.`content_id` = `FEEDENTRYCONTENTS`.`id` 
INNER JOIN `FEEDENTRYSTATUSES`  ON `FEEDENTRYSTATUSES`.`subscription_id` = `FEEDSUBSCRIPTIONS`.`id` 