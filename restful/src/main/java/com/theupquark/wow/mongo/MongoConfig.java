package com.theupquark.wow.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

  @Bean
  public Mongo mongo() throws Exception {
    return new MongoClient("localhost");
  }

  @Bean
  public MongoTemplate mongoTemplate() throws Exception {
    return new MongoTemplate(this.mongo(), "wow");
  }

}
