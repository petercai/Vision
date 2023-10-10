/*
 * Copyright (c) 2020. Peter Cai
 */

package cai.peter.vision.rest.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.peppermint.vision.VisionApplication;

/**
 * A unit test covers a single “unit”, where a unit commonly is a single class, but can also be a
 * cluster of cohesive classes that is tested in combination.
 *
 * <p>An integration test can be any of the following: - test that covers multiple “units”. It tests
 * the interaction between two or more clusters of cohesive classes. - test that covers multiple
 * layers. This is actually a specialization of the first case and might cover the interaction
 * between a business service and the persistence layer, for instance. - test that covers the whole
 * path through the application. In these tests, we send a request to the application and check that
 * it responds correctly and has changed the database state according to our expectations.
 *
 * <p>Spring Boot provides the @SpringBootTest annotation which we can use to create an application
 * context containing all the objects we need for all of the above test types. Note, however, that
 * overusing @SpringBootTest might lead to very long-running test suites.
 *
 * <p>So, for simple tests that cover multiple units we should rather create plain tests, very
 * similar to unit tests, in which we manually create the object graph needed for the test and mock
 * away the rest. This way, Spring doesn’t fire up a whole application context each time the test is
 * started.
 *
 * <p>For tests that cover integration with the web layer or persistence layer, we can
 * use @WebMvcTest or @DataJpaTest instead. For integration with other layers, have a look at Spring
 * Boot’s other test slice annotations. Note that these test slices will also take some time to boot
 * up, though.
 *
 * <p>Finally, for tests that cover the whole Spring Boot application from incoming request to
 * database, or tests that cover certain parts of the application that are hard to set up manually,
 * we can and should use @SpringBootTest. When we are unit testing a rest service, we would want to
 * launch only the specific controller and the related MVC Components. @WebMvcTest annotation is
 * used for unit testing Spring MVC application. This can be used when a test focuses only Spring
 * MVC components. Using this annotation will disable full auto-configuration and only apply
 * configuration relevant to MVC tests. @RunWith(SpringRunner.class) : SpringRunner is short hand
 * for SpringJUnit4ClassRunner which extends BlockJUnit4ClassRunner providing the functionality to
 * launch a Spring TestContext Framework. @WebMvcTest(value = CategoryController.class): WebMvcTest
 * annotation is used for unit testing Spring MVC application. This can be used when a test focuses
 * only Spring MVC components. In this test, we want to launch only CategoryController. All other
 * controllers and mappings will not be launched when this unit test is executed.
 *
 * <p>MockMvcRequestBuilders.get("url").accept(MediaType.APPLICATION_JSON): Creating a Request
 * builder to be able to execute a get request to uri “url” with accept header as “application/json”
 * mockMvc.perform(requestBuilder).andReturn(): mockMvc is used to perform the request and return
 * the response back. @SpringBootTest by default starts searching in the current package of the test
 * class and then searches upwards through the package structure, looking for a class annotated
 * with @SpringBootConfiguration from which it then reads the configuration to create an application
 * context.
 *
 * <p>This class is usually our main application class since the @SpringBootApplication annotation
 * includes the @SpringBootConfiguration annotation. It then creates an application context very
 * similar to the one that would be started in a production environment.
 *
 * <p>Because we have a full application context, including web controllers, Spring Data
 * repositories, and data sources, @SpringBootTest is very convenient for integration tests that go
 * through all layers of the application:
 */

/**
 * @ExtendWith The code examples in this tutorial use the @ExtendWith annotation to tell JUnit 5 to
 * enable Spring support. As of Spring Boot 2.1, we no longer need to load the SpringExtension
 * because it's included as a meta annotation in the Spring Boot test annotations
 * like @DataJpaTest, @WebMvcTest, and @SpringBootTest.
 *
 * <p>Here, we use @AutoConfigureMockMvc to add a MockMvc instance to the application context. We
 * could use Spring’s standard @ContextConfiguration support to load only one of our module
 * configurations above, but this way we won’t have support for Spring Boot’s test annotations
 * like @SpringBootTest, @WebMvcTest, and @DataJpaTest which conveniently set up an application
 * context for integration tests.
 *
 * <p>By default, the test annotations mentioned above create an application for the
 * first @SpringBootConfiguration annotation they find from the current package upwards, w hich is
 * usually the main application class, since the @SpringBootApplication annotation includes
 * a @SpringBootConfiguration.
 *
 * <p>@SpringBootConfiguration @EnableAutoConfiguration class CustomerTestConfiguration extends
 * CustomerConfiguration {}
 *
 * <p>Each test configuration is annotated with @SpringBootConfiguration to make it discoverable
 * by @SpringBootTest and its companions and extends the “real” configuration class to inherit its
 * contributions to the application context.
 *
 * <p>Also, each configuration is additionally annotated with @EnableAutoConfiguration to enable
 * Spring Boot’s auto-configuration magic.
 */

/*
reference:  https://reflectoring.io/spring-boot-test/
            https://reflectoring.io/testing-verticals-and-layers-spring-boot/?utm_source=pocket-chrome-recs
            https://reflectoring.io/spring-boot-web-controller-test/
 */

@ExtendWith(SpringExtension.class)
// @SpringBootTest(classes = VisionApplication.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = VisionApplication.class)
@AutoConfigureMockMvc
// @WebMvcTest(value = CategoryController.class)
@WithMockUser
public class CategoryControllerTest /*extends RestTestBase*/ {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testGetSubscriptions() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
          "/rest/category/get").accept(
          MediaType.APPLICATION_JSON);
        MvcResult result = mvc
          .perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        assertEquals(200, status);
    }
}
