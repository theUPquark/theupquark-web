package com.theupquark.wow.mongo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.theupquark.wow.models.Achievement;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;

public class AchievementStore {

  private ObjectMapper objectMapper;
  private MongoTemplate mongoTemplate;
  private List<Achievement> knownAchievements;
  private String uriAchievementTemplate;

  public AchievementStore(MongoTemplate mongoTemplate) {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.mongoTemplate = mongoTemplate;
    this.uriAchievementTemplate = "https://us.api.battle.net/wow/achievement/%s?locale=en_US&apikey=%s";

    this.knownAchievements = this.mongoTemplate.findAll(Achievement.class);
  }

  public Achievement getAchievementDetails(String id, String apiKey) {
    Optional<Achievement> optionalAchievement = this.knownAchievements.stream()
      .filter(achie -> achie.getId() == id)
      .findFirst();

    if (optionalAchievement.isPresent()) {
      return optionalAchievement.get();
    }

    Achievement queriedAchievement = this.queryAchievementDetails(id, apiKey);

    if (queriedAchievement == null) {
      return null;
    }

    this.knownAchievements.add(queriedAchievement);
    this.mongoTemplate.insert(queriedAchievement);

    return queriedAchievement;
  }

  public Achievement queryAchievementDetails(String id, String apiKey) {
    try {
      return this.objectMapper.readValue(
            URI.create(
              String.format(
                this.uriAchievementTemplate,
                id,
                apiKey)).toURL(), Achievement.class);

    } catch (Throwable t) {
      System.out.println("Query result had error with : " 
          + String.format(this.uriAchievementTemplate, id, apiKey));
      System.out.println(t);
      return null;
    }
  }
}
