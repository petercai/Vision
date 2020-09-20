/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.persistence.dao;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.persistence.entity.User;
import cai.peter.vision.persistence.repository.UsersRepository;
import cai.peter.vision.rest.dto.UnreadCount;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;


@SpringBootTest(classes = VisionApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
class SubscriptionDAOTest {


    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    SubscriptionDAO subscriptionDAO;

    @Autowired
    UsersRepository userRepo;

    @Test
    void testGetUnreadCount() {
        User peter = userRepo.getPeter();
        List<UnreadCount> result = subscriptionDAO.getUnreadCount(peter);
        result.stream().forEach(c->log.info(c.toString()));

    }
}
