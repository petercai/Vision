Select
    FEEDSUBSCRIPTIONS.feed_id,
    FEEDSUBSCRIPTIONS.title,
    Count(FEEDS.id) As count,
    Max(FEEDENTRIES.updated) As latest_update
From
    FEEDENTRIES Inner Join
    FEEDS On FEEDENTRIES.feed_id = FEEDS.id Inner Join
    FEEDSUBSCRIPTIONS On FEEDSUBSCRIPTIONS.feed_id = FEEDS.id Left Join
    FEEDENTRYSTATUSES On FEEDENTRYSTATUSES.entry_id = FEEDENTRIES.id
            And FEEDENTRYSTATUSES.subscription_id = FEEDSUBSCRIPTIONS.id
Where
    FEEDSUBSCRIPTIONS.user_id = 2000 And
    (FEEDENTRYSTATUSES.read_status = False Or
        FEEDENTRYSTATUSES.read_status Is Null)
Group By
    FEEDSUBSCRIPTIONS.feed_id,
    FEEDSUBSCRIPTIONS.title,
    FEEDENTRYSTATUSES.read_status