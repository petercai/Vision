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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

@SpringBootTest(classes = {VisionApplication.class})
@RunWith(SpringRunner.class)
public class OPMLImporterTest {

    @Autowired
    OPMLImporter importer;

    @Autowired
    UsersRepository userRepo;

    @Autowired
    FeedRefreshTaskGiver job;

    @Autowired
    FeedQueues queue;

    @Before
    public void setUp() throws Exception {
        job.start();
        job.setWaitInterval(1);
    }

    @After
    public void cleanup(){
        job.stop();
    }

    @Test
    public void importOpml() throws IOException, InterruptedException {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("myfeed.opml");
        String opml = IOUtils.toString(input, "UTF-8");
        User admin = userRepo.getAdmin();
        importer.importOpml(admin, opml);

        while( !queue.isAllDone() )
        {
            Thread.sleep(100L );
        }
    }
}