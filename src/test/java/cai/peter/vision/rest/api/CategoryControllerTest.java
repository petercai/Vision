/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.rest.api;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.common.VisionConfiguration;
import cai.peter.vision.common.VisionConfiguration.ApplicationSettings;
import cai.peter.vision.persistence.entity.FeedCategory;
import cai.peter.vision.persistence.entity.FeedSubscription;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.persistence.repository.FeedcategoriesRepository;
import cai.peter.vision.persistence.repository.FeedentrystatusesRepository;
import cai.peter.vision.persistence.repository.FeedsubscriptionsRepository;
import cai.peter.vision.persistence.service.FeedEntryService;
import cai.peter.vision.persistence.service.FeedSubscriptionService;
import cai.peter.vision.rest.dto.Category;
import cai.peter.vision.rest.dto.UnreadCount;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;


import javax.ws.rs.core.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;



@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = VisionApplication.class)
@AutoConfigureMockMvc
public class CategoryControllerTest /*extends RestTestBase*/{
    @Autowired
    private MockMvc mvc;


    @Test
    public void testGetSubscriptions() throws Exception {
        String uri = "/get";
        MvcResult result = mvc
          .perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme