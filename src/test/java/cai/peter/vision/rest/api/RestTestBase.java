/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.rest.api;

import cai.peter.vision.VisionApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = VisionApplication.class)
@WebAppConfiguration
public class RestTestBase {
  protected MockMvc mvc;
  @Autowired
  WebApplicationContext applicationContext;

  ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  public void setup(){
    mvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
  }

  protected String toJson(Object obj) throws JsonProcessingException {
    return mapper.writeValueAsString(obj);
  }

  protected <T> T fromJson(String val, Class<T> claz) throws JsonProcessingException {
    return mapper.readValue(val, claz);
  }


}
