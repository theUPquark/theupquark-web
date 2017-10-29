package com.theupquark.wow.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.theupquark.wow.models.Achievement;
import com.theupquark.wow.models.AchievementsProfile;
import com.theupquark.wow.models.BNetAccount;
import com.theupquark.wow.models.Character;
import com.theupquark.wow.models.WebAppSettings;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class WowAchievementAdapter {

  private ObjectMapper objectMapper;
  private String uriTemplate;

  public WowAchievementAdapter() {
    this.objectMapper = new ObjectMapper();
    this.uriTemplate = "https://us.api.battle.net/wow/character/%s/%s?fields=achievements&locale=en_US&apikey=%s";
  }

  public String queryAchievements(String name, String server, String apiKey) {

    try {
      AchievementsProfile achievementProfile = 
        objectMapper.readValue(
            URI.create(
              String.format(
                this.uriTemplate, 
                server, 
                name, 
                apiKey)).toURL(), AchievementsProfile.class);


      return "AchievementProfile for with " + achievementProfile.getAchievements().get("achievementsCompleted").size() + "achievement entries.";

    } catch (Throwable t) {
      System.out.println(t.getMessage());
      return t.getMessage();
    }
  }

  public AchievementsProfile queryAchievementsProfile(String name, String server, String apiKey) {

    try {
      AchievementsProfile achievementProfile = 
        objectMapper.readValue(
            URI.create(
              String.format(
                this.uriTemplate, 
                server, 
                name, 
                apiKey)).toURL(), AchievementsProfile.class);


      return achievementProfile;

    } catch (Throwable t) {
      System.out.println(t.getMessage());
      return null;
    }

  }
  
  public List<Achievement> mapAchievements(AchievementsProfile achievementsProfile) {

    int achievementsCompleted = achievementsProfile.getAchievements().get("achievementsCompleted").size();

    List<Achievement> achievements = new ArrayList<>();

    for (int i = 0; i < achievementsCompleted; i++) {
      Achievement achievement = new Achievement();
      achievement.setId(Long.parseLong(
            achievementsProfile.getAchievements().get("achievementsCompleted").get(i)));
      achievement.setTime(Long.parseLong(
            achievementsProfile.getAchievements().get("achievementsCompletedTimestamp").get(i)));
      //TODO Add character name/server to each achievement
      //achievement.setEarnedBy(new ArrayList<>().add(new Character()));
      achievements.add(achievement);
    }

    return achievements;
  }


  public List<Achievement> compare(WebAppSettings webAppSettings, String apiKey) {

    List<List<Achievement>> separatedAchievements = new ArrayList<>();

    for (BNetAccount account : webAppSettings.getUsers()) {
      for (String server : account.getCharacterMap().keySet()) {
        List<String> characterList = account.getCharacterMap().get(server);
        List<Achievement> accountAchievements = new ArrayList<>();

        for (String character : characterList) {
          accountAchievements.addAll(
              this.mapAchievements(this.queryAchievementsProfile(character, server, apiKey)));
        }
        separatedAchievements.add(accountAchievements);
      }
    }
    
    

    return this.obtainDuplicates(separatedAchievements);
  }


  public List<Achievement> obtainDuplicates(List<List<Achievement>> separatedAchievements) {
    List<Achievement> duplicates = new ArrayList<>();
    int userCount = separatedAchievements.size();

    if (userCount < 2) {
      return duplicates;
    }

    List<Achievement> user1Achievements = separatedAchievements.get(0);
    for (Achievement achievement : user1Achievements) {
      for (int i = 1; i < userCount; i++) {
        for (Achievement otherAchievement : separatedAchievements.get(i)) {
          if (achievement.equals(otherAchievement)) {
            duplicates.add(achievement);
            break;
          }
        }
      }
    }

    return duplicates;
  }

}
