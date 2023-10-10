package cai.peter.vision.persistence.repository;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.peppermint.vision.VisionApplication;
import com.peppermint.vision.persistence.entity.FeedEntryContent;
import com.peppermint.vision.persistence.repository.FeedentrycontentsRepository;

@SpringBootTest(classes = VisionApplication.class)
@ExtendWith(SpringExtension.class)
public class FeedentrycontentsRepositoryTest {
    /**
     * Logger for this class
     */
    private static final Logger logger =
            LoggerFactory.getLogger(FeedentrycontentsRepositoryTest.class);

    @Autowired
    FeedentrycontentsRepository repo;



    @Test
    public void testFindAll() throws Exception {
        List<FeedEntryContent> all = repo.findAll();
        for (FeedEntryContent t : all) {
            logger.info("testFindAll() - FeedEntryContent t2={}", t.getContent()); //$NON-NLS-1$
        }
    }
}
