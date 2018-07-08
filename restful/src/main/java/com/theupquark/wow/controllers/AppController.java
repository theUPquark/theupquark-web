package com.theupquark.wow.controllers;

import com.mongodb.MongoClient;
import com.theupquark.wow.adapters.WowAchievementAdapter;
import com.theupquark.wow.models.Achievement;
import com.theupquark.wow.models.AchievementsProfile;
import com.theupquark.wow.models.CompareCharacterRequest;
import com.theupquark.wow.mongo.AchievementStore;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class AppController {

  @Value("${com.theupquark.wow.apikey}")
  private String apiKey;

  private WowAchievementAdapter wowAchievementAdapter;

  public AppController() {
    this.wowAchievementAdapter = 
      new WowAchievementAdapter(new AchievementStore(
            new MongoTemplate(new MongoClient("localhost"), "wow"))); 

  }


  @RequestMapping("/test")
  @ResponseBody
  public List<Achievement> test() {
      return this.wowAchievementAdapter.test(this.apiKey);
  }


  /**
   * Returns profile
   * @param server server
   * @param character character name
   * @param region region
   * @return AchievementsProfile
   */
  @RequestMapping("/query/{region}/{server}/{character}/")
  @ResponseBody
  public AchievementsProfile query(
      @PathVariable(value="server") String server, 
      @PathVariable(value="character") String character, 
      @PathVariable(value="region") String region) {
    return this.wowAchievementAdapter.queryAchievementsProfile(
        character, server, region, "en_US", this.apiKey);
  }


  public static void main(String[] args) throws Exception {
      SpringApplication.run(AppController.class, args);
  }

  /**
   * Takes in request and returns the achievements that all characters
   * would have completed together
   *
   * @param request contains character and region information
   * @return list of achievements
   */
  @RequestMapping("/compare")
  @ResponseBody
  public List<Achievement> compare(
      @RequestBody CompareCharacterRequest request) {

    return this.wowAchievementAdapter.compare(request, this.apiKey);
  }
}
