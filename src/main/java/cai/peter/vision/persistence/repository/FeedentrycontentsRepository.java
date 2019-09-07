/*
 * Created on 2019-08-02 ( Date ISO 2019-08-02 - Time 22:20:41 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.0.0
 */
package cai.peter.vision.persistence.repository;

import cai.peter.vision.persistence.entity.FeedEntryContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring data Jpa repository for "Feedentrycontents" <br>
 * @author Telosys (http://www.telosys.org/) version 3.0.0
 */
@Repository
public interface FeedentrycontentsRepository extends JpaRepository<FeedEntryContent, Long>, JpaSpecificationExecutor<FeedEntryContent> {
    @Query(value = "select c.id from FeedEntryContent c where c.contentHash = ?1 and c.titleHash = ?2")
    Long findExisting(String contentHash, String titleHash);
}