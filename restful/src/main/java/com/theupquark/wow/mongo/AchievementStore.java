package com.theupquark.wow.mongo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.theupquark.wow.models.Achievement;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * This is the connection to the mongo database storing information about 
 * Achievements.
 */
public class AchievementStore {

  private static final Logger LOG = LoggerFactory.getLogger(
      AchievementStore.class);
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
    LOG.info("Started {} with {} known achievements",
        this.getClass().getSimpleName(), this.knownAchievements.size());

    if (LOG.isTraceEnabled()) {
      for (Achievement achie : this.knownAchievements) {
        LOG.trace("Known achie id: {}", achie.getId());
      }
    }
  }

  /**
   * Really only for testing
   *
   * @param knownAchievements list of achievments
   */
  protected void setKnownAchievements(List<Achievement> knownAchievements) {
    this.knownAchievements = knownAchievements;
  }

  /**
   * Obtain additional information about an achievement. Attempt to obtain 
   * details from data store before making an API call
   *
   * @param id achievement identifier
   * @param apiKey wow web api key
   */
  public Achievement getAchievementDetails(String id, String apiKey) {
    // Check achievement list in memory first
    Optional<Achievement> optionalAchievement = this.knownAchievements.stream()
      .filter(achie -> id.equals(achie.getId()))
      .findFirst();

    if (optionalAchievement.isPresent()) {
      LOG.info("Found achievement {} within store already.", id);
      return optionalAchievement.get();
    }

    // Check achievement list within mongodb
    Achievement dbAchievement = this.mongoTemplate.findById(
      id, Achievement.class);
    if (dbAchievement != null) {
      LOG.warn("Somehow missing achievement {} locally but its in db", id);
      this.knownAchievements.add(dbAchievement);
      return dbAchievement;
    }

    // Seems we're missing details here, so pull from API
    Achievement queriedAchievement = this.queryAchievementDetails(id, apiKey);

    if (queriedAchievement == null) {
      return null;
    }

    LOG.info("Adding achievement {} to known list and mongo", id);
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
