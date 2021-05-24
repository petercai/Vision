package cai.peter.test.common;

import cai.peter.vision.VisionApplication;
import cai.peter.vision.test.model.Endpoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes= VisionApplication.class)
@ExtendWith(SpringExtension.class)
public class SpringTestBase extends FxRestAssured{


//  @Autowired
//  protected FxMongo mg;
  protected String objectName;

  @Autowired
  @Qualifier("commafeed")
  protected Endpoint endpoint;

  @Autowired
  @Qualifier("commafeed-ui")
  protected Endpoint endpointUI;

  @Autowired
  @Qualifier("commafeed-admin")
  protected Endpoint endpointAdmin;



  protected void initFeed() throws JsonProcessingException {
    logInit();
    feedLogin(endpoint.getUser(), endpoint.getPassword(), endpoint.getUrl());
  }

  protected void initTesting() {
    logInit();

//    MongoCollection<Endpoint> serviceRepo = mg.getServiceRepo();
//    endpoint = serviceRepo.find(eq("active", true)).first();
//    formLogin(endpoint.getUser(), endpoint.getPassword(), endpoint.getUrl());
  }

    public void printCollection(Collection collection) {
        System.out.println("{" +
            collection.stream().sorted().map(s->"\n\""+s+"\"").collect(Collectors.joining(","))
            + "\n}"
        );

    }
}
