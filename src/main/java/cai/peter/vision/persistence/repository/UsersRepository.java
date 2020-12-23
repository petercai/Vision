/*
 * Created on 2019-08-02 ( Date ISO 2019-08-02 - Time 22:20:41 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.0.0
 */
package cai.peter.vision.persistence.repository;

import cai.peter.vision.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring data Jpa repository for "Users" <br>
 * @author Telosys (http://www.telosys.org/) version 3.0.0
 */
@Repository
public interface UsersRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query(value = "select u from User u where u.name = 'admin'")
    public User getAdmin();
    @Query(value = "select u from User u where u.name = ?1")
    public User getUser(String userName);

    @Query(value = "select u from User u where u.name = ?1 or u.email = ?1")
    User findByName(String nameOrEmail);
}