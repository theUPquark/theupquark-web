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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


      return "AchievementProfile for with " + achievementProfile.getAchievements().get("achievementsCompleted").size() + " achievement entries.";

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
      Achievement achievement = achievementsProfile.getAchievementByIndex(i);
      //TODO Add character name/server to each achievement
      //achievement.setEarnedBy(new ArrayList<>().add(new Character()));
      achievements.add(achievement);
    }

    return achievements;
  }


  public List<Achievement> compare(List<Character> characters, String apiKey) {

    List<List<Achievement>> list = new ArrayList<>();

    for (Character character : characters) {
      List<Achievement> achievementList = this.mapAchievements(this.queryAchievementsProfile(
            character.getName(), character.getServer(), apiKey
            ));
      list.add(achievementList); 
    }


    return this.obtainDuplicates(list);
  }

  public List<Achievement> test(String apiKey) {
    List<Character> characters = new ArrayList<>();
    Character tuei = new Character();
    tuei.setServer("argent-dawn");
    tuei.setName("errai");
    
    Character rhetaiya = new Character();
    rhetaiya.setServer("argent-dawn");
    rhetaiya.setName("rhetaiya");

    characters.add(tuei);
    characters.add(rhetaiya);

    return this.compare(characters, apiKey);
  }

  public List<Achievement> obtainDuplicates(List<List<Achievement>> listOfLists) {

    for (List<Achievement> list : listOfLists) {
      System.out.println("List with " + list.size() + " achievements");
    }
    // Use the first list as a baseline
    List<Achievement> duplicates = new ArrayList<>();
    List<Achievement> baseline = listOfLists.get(0);

    //Stream<Achievement> stream = duplicates.stream();

    // Loop the remaining lists and filter by duplicates
    for (Achievement achieve : baseline) {
      boolean duplicate = false;
      for (int i = 1; i < listOfLists.size(); i++) {
        /*
        if (!listOfLists.get(i).contains(achieve)) {
          duplicate = false;
        }
        */
        for (Achievement innerAchieve : listOfLists.get(i)) {
          if (achieve.equals(innerAchieve)) {
            duplicate = true;
          }
        }
      }

      if (duplicate) {
        duplicates.add(achieve);
      }
    }

    return duplicates;
  }

}
