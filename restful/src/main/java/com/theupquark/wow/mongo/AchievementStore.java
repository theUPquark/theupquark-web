package com.theupquark.wow.mongo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.theupquark.wow.models.Achievement;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * This is the connection to the mongo database storing information about 
 * Achievements.
 */
public class AchievementStore {

  private ObjectMapper objectMapper;
  private MongoTemplate mongoTemplate;
  private List<Achievement> knownAchievements;
  private String uriAchievementTemplate;

  /**
   * Constructor.
   * Connects to mongo and pulls out the currently known achievements into memory.
   *
   * @param mongoTemplate connection to mongo
   */
  public AchievementStore(MongoTemplate mongoTemplate) {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.mongoTemplate = mongoTemplate;
    this.uriAchievementTemplate = "https://us.api.battle.net/wow/achievement/%s?locale=en_US&apikey=%s";

    this.knownAchievements = this.mongoTemplate.findAll(Achievement.class);
  }

  /**
   * Obtain additional information about an achievement. Attempt to obtain 
   * details from data store before making an API call
   *
   * @param id achievement identifier
   * @param apiKey wow web api key
   */
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

  /**
   * API call to obtain information about an Achievement
   *
   * @param id achievement id
   * @param apiKey wow web api key
   * @return Achievement with additional data
   */
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
