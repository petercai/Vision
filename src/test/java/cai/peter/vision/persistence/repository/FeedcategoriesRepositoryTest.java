package cai.peter.vision.persistence.repository;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.FeedCategory;
import cai.peter.vision.persistence.entity.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = VisionApplication.class)
//@DataJpaTest
public class FeedcategoriesRepositoryTest /*implements FeedcategoriesRepository*/ {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory
        .getLogger(FeedcategoriesRepositoryTest.class);

    @Autowired
    FeedcategoriesRepository feedcategoriesRepository;
    @Autowired
    UsersRepository userRepo;


    @Test
    public void testFindAll() throws Exception {
        User admin = userRepo.getAdmin();

        List<FeedCategory> all = feedcategoriesRepository.getByUser(admin);
        for (FeedCategory cat : all) {
            logger.info("testFindAll() - FeedCategory c={}", cat); //$NON-NLS-1$
        }
    }
}
