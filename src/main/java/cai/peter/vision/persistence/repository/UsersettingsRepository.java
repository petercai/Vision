/*
 * Created on 2019-08-02 ( Date ISO 2019-08-02 - Time 22:20:41 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.0.0
 */
package cai.peter.vision.persistence.repository;

import cai.peter.vision.persistence.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring data Jpa repository for "Usersettings" <br>
 * @author Telosys (http://www.telosys.org/) version 3.0.0
 */
@Repository
public interface UsersettingsRepository extends JpaRepository<UserSettings, Long>, JpaSpecificationExecutor<UserSettings> {
}