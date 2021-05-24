package cai.peter.vision.test.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javax.annotation.PostConstruct;


import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;
import org.mongojack.ObjectMapperConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class FxMongo {

  @Autowired protected SimpleMongoClientDatabaseFactory mongoFactory;

  private MongoDatabase mongoDatabase;

  private MongoCollection<Endpoint> serviceRepo;

  private ObjectMapper objectMapper = JacksonUtil.createMapper();

  @PostConstruct
  protected void mongoInit() {
    ObjectMapperConfigurer.configureObjectMapper(objectMapper);
    mongoDatabase = mongoFactory.getMongoDatabase();

    serviceRepo = getMongoCollection("endpoints", Endpoint.class);

  }

  public <T> MongoCollection<T> getMongoCollection( String name, Class<T> clz) {
    return JacksonMongoCollection.builder()
      .withObjectMapper(objectMapper)
      .build(mongoDatabase, name, clz, UuidRepresentation.STANDARD);
  }
}
