/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.feed.opml;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.feed.FeedQueues;
import cai.peter.vision.feed.FeedRefreshTaskGiver;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.persistence.repository.UsersRepository;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;


@SpringBootTest(classes = {VisionApplication.class})
@ExtendWith(SpringExtension.class)
public class OPMLImporterTest {

    @Autowired
    OPMLImporter importer;

    @Autowired
    UsersRepository userRepo;

    @Autowired
    FeedRefreshTaskGiver job;

    @Autowired
    FeedQueues queue;

    @BeforeAll
    public void setUp() throws Exception {
        job.setWaitInterval(1);
        job.process();
    }


    @Test
    public void importOpml() throws IOException, InterruptedException {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("myfeed.opml");
        String opml = IOUtils.toString(input, "UTF-8");
        User admin = userRepo.getAdmin();
        importer.importOpml(admin, opml);

        while( queue.isAllDone() > 0 )
        {
            Thread.sleep(100L );
        }
            Thread.sleep(5000L );
    }
}