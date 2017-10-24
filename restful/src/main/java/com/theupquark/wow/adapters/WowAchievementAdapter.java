package com.theupquark.wow.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.theupquark.wow.models.AchievementsProfile;

import java.net.URI;

public class WowAchievementAdapter {

  private ObjectMapper objectMapper;
  private String uriTemplate;
  private String apiKey;

  public WowAchievementAdapter() {
    this.objectMapper = new ObjectMapper();
    this.uriTemplate = "https://us.api.battle.net/wow/character/%s/%s?fields=achievements&locale=en_US&apikey=%s";
    this.apiKey = "";
  }

  public String queryAchievements(String name, String server) {

    try {
      AchievementsProfile achievementProfile = 
        objectMapper.readValue(
            URI.create(
              String.format(
                this.uriTemplate, 
                server, 
                name, 
                this.apiKey)).toURL(), AchievementsProfile.class);


      return "AchievementProfile for with " + achievementProfile.getAchievements().get("achievementsCompleted").size() + "achievement entries.";

    } catch (Throwable t) {
      System.out.println(t.getMessage());
      return t.getMessage();
    }
  }



}
