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


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


//@WebAppConfiguration
//@SpringBootTest(classes = VisionApplication.class)
//@RunWith(SpringJUnit4ClassRunner.class)
public class CategoryControllerTest extends RestTestBase{
    @Mock
    FeedcategoriesRepository feedCategoryDAO;
    @Mock
    FeedentrystatusesRepository feedEntryStatusDAO;
    @Mock
    FeedsubscriptionsRepository feedSubscriptionDAO;
    @Mock
    FeedEntryService feedEntryService;
    @Mock
    FeedSubscriptionService feedSubscriptionService;
    @Mock
    VisionConfiguration config;
    @Mock
    Logger log;
    @InjectMocks
    CategoryController categoryController;


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