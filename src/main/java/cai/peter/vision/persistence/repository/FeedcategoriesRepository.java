/*
 * Created on 2019-08-02 ( Date ISO 2019-08-02 - Time 22:20:41 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.0.0
 */
package cai.peter.vision.persistence.repository;

import cai.peter.vision.persistence.entity.FeedCategory;
import cai.peter.vision.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring data Jpa repository for "Feedcategories" <br>
 * @author Telosys (http://www.telosys.org/) version 3.0.0
 */
@Repository
public interface FeedcategoriesRepository extends JpaRepository<FeedCategory, Long>, JpaSpecificationExecutor<FeedCategory> {
    @Query(value = "select c from FeedCategory c where c.user = ?1 and c.name = ?2 and c.parent = ?3")
    FeedCategory findByName(User user, String name, FeedCategory parent);

//    @Query(value = "select c from FeedCategory c where c.user = ?1")
    List<FeedCategory> getByUser(User user);
}