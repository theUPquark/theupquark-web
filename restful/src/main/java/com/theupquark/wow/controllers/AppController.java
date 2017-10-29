package com.theupquark.wow.controllers;

import com.theupquark.wow.adapters.WowAchievementAdapter;
import com.theupquark.wow.models.WebAppSettings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class AppController {

  @Value("${com.theupquark.wow.apikey}")
  private String apiKey;

  private WowAchievementAdapter wowAchievementAdapter;

  public AppController() {
    this.wowAchievementAdapter = new WowAchievementAdapter();

  }


  @RequestMapping("/process")
  @ResponseBody
  String home() {
      return apiKey;
  }


  @RequestMapping("/query/{server}/{character}/")
  @ResponseBody
  public String query(@PathVariable(value="server") String server, @PathVariable(value="character") String character) {
    return this.wowAchievementAdapter.queryAchievements(character, server, this.apiKey);
  }


  public static void main(String[] args) throws Exception {
      SpringApplication.run(AppController.class, args);
  }

  @RequestMapping("/compare")
  @ResponseBody
  public String compare(@RequestBody WebAppSettings webAppSettings) {

    return "Success";
  }
}
