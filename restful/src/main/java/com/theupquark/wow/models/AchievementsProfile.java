package com.theupquark.wow.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AchievementsProfile {

  private long lastModified;
  private String name;
  private String realm;
  private String battlegroup;
  private int achievementPoints;
  private Map<String, List<String>> achievements;

  /**
   * AchievementsProfile
   * achievements
   *    achievementsCompleted: IDs
   *    achievementsCompletedTimestamp: times
   *    criteria: no idea
   *
  public AchievementsProfile() {
    this.lastModified = 0;
    this.name = "";
    this.realm = "";
    this.battlegroup = "";
    this.achievementPoints = 0;
    this.achievements = new HashMap<>();
  }

  public void setAchievements(Map<String, List<String>> achievements) {
    this.achievements = achievements;
  }

  public Map<String, List<String>> getAchievements() {
    return this.achievements;
  }
}
