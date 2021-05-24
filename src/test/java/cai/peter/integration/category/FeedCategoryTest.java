package cai.peter.integration.category;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import cai.peter.test.common.SpringTestBase;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FeedCategoryTest extends SpringTestBase {

    public static final String OBJECT_NAME = "category";

    {
        objectName = OBJECT_NAME;
    }

    @BeforeEach
    public void setup() throws JsonProcessingException {
        initFeed();
    }

    @Test
    public void testGetCategory() throws Exception {
        Response response = buildCookieRequest().get(endpoint.getUrl() + "/" + OBJECT_NAME + "/get");
        saveJson((List) response.path("children"), objectName);
    }

    @Test
    public void testRetrieveAllObjects() throws Exception {

        String url = endpoint.getUrl() + OBJECT_NAME;
        Response response = buildAuthenticatedRequest()
                .get(url);
        response.then().statusCode(200);
        saveJson(response.asString());
        saveJson((List) response.path("data"), objectName);

    }

    @Test
    public void updateObjects() {
        String body = readJsonFile("00S00000003M001.json");
        buildAuthenticatedRequest()
                .body(body)
                .put(endpoint.getUrl() + OBJECT_NAME)
                .then()
                .statusCode(200);

    }


}
