package com.theupquark.wow.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.theupquark.wow.models.Achievement;
import com.theupquark.wow.models.AchievementsProfile;
import com.theupquark.wow.models.BNetAccount;
import com.theupquark.wow.models.Character;
import com.theupquark.wow.models.WebAppSettings;
import com.theupquark.wow.mongo.AchievementStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WowAchievementAdapter {

  private ObjectMapper objectMapper;
  private String uriTemplate;
  private String uriAchievementTemplate;
  private AchievementStore achievementStore;

  public WowAchievementAdapter(AchievementStore achievementStore) {
    this.achievementStore = achievementStore;
    this.objectMapper = new ObjectMapper();
    this.uriTemplate = "https://us.api.battle.net/wow/character/%s/%s?fields=achievements&locale=en_US&apikey=%s";
    this.uriAchievementTemplate = "https://us.api.battle.net/wow/achievement/%s?locale=en_US&apikey=%s";
  }

  /**
   * This method is basically dead
   *
   * @param name character name
   * @param server server name
   * @param apiKey wow api key
   * @return informational string testing api call
   */
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

  /**
   * Use a Character's name and server to obtain a list the achievements
   * profile.
   *
   * @param name character name
   * @param server server name
   * @param apiKey wow web api key
   */
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

  /**
   * Takes the AchievementsProfile, and return a list of Achievements.
   *
   * @param achievementsProfile profile containing achievement ids and timestamps
   * @return list of Achievement
   */  
  public List<Achievement> mapAchievements(AchievementsProfile achievementsProfile) {

    int achievementsCompleted = 
      achievementsProfile.getAchievements().get("achievementsCompleted").size();

    List<Achievement> achievements = new ArrayList<>();

    // Populate fields for Id and Time
    // Title, points, description will come later once lists are compared
    for (int i = 0; i < achievementsCompleted; i++) {
      Achievement achievement = achievementsProfile.getAchievementByIndex(i);
      achievements.add(achievement);
    }

    return achievements;
  }


  /**
   * Take in a list of characters and return common list of achievements
   *
   * @param characters list of characters
   * @param apiKey wow web api key
   * @return list of achievemnts completed by characters together
   */
  public List<Achievement> compare(List<Character> characters, String apiKey) {

    List<List<Achievement>> list = new ArrayList<>();

    for (Character character : characters) {
      List<Achievement> achievementList = 
        this.mapAchievements(this.queryAchievementsProfile(
            character.getName(), 
            character.getServer(), 
            apiKey));
      list.add(achievementList); 
    }

    List<Achievement> matches = this.obtainDuplicates(list);
    this.mapAdditionalFields(matches, apiKey);

    return this.obtainDuplicates(list);
  }

  /**
   * Temp method to obtain and compare achievements
   *
   * @param apiKey wow web api key
   * @return list of achievements that match by time between characters
   */
  public List<Achievement> test(String apiKey) {
    List<Character> characters = new ArrayList<>();
    Character errai = new Character();
    errai.setServer("argent-dawn");
    errai.setName("errai");
    
    Character rhetaiya = new Character();
    rhetaiya.setServer("argent-dawn");
    rhetaiya.setName("rhetaiya");

    characters.add(errai);
    characters.add(rhetaiya);

    return this.compare(characters, apiKey);
  }

  /**
   * This method takes in a list of list of achievements.
   * The returned Achievement list only obtains those where the same achievement
   * (id) was completed at the same time (within a margin or error)
   *
   * @param listOfLists list of list of achievements
   * @return reduced list of achievements
   */
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
      boolean duplicate = true;
      // If any other list doesn't contain achievement it's not a duplicate
      for (int i = 1; i < listOfLists.size(); i++) {
        // Uses equals method in Achievement overrided to check ids and time only
        if (!listOfLists.get(i).contains(achieve)) {
          duplicate = false;
        }
      }

      if (duplicate) {
        duplicates.add(achieve);
      }
    }

    return duplicates;
  }

  /**
   * Include information to each achievement, such as title, points, etc.
   *
   * @param achievements list to include fields to
   * @param apiKey wow web api key
   */
  public void mapAdditionalFields(List<Achievement> achievements, String apiKey) {
    for (Achievement achievement : achievements) {
      Achievement detailedAchievement = this.achievementStore.getAchievementDetails(achievement.getId(), apiKey);

      achievement.setTitle(detailedAchievement.getTitle());
      achievement.setPoints(detailedAchievement.getPoints());
      achievement.setDescription(detailedAchievement.getDescription());
    }
  }
}
