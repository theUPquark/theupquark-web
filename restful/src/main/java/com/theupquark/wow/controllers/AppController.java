package com.theupquark.wow.controllers;

import com.theupquark.wow.adapters.WowAchievementAdapter;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class AppController {

  private WowAchievementAdapter wowAchievementAdapter;

  public AppController() {
    this.wowAchievementAdapter = new WowAchievementAdapter();

  }


  @RequestMapping("/process")
  @ResponseBody
  String home() {
      return "Hello World!";
  }


  @RequestMapping("/query/{server}/{character}/")
  @ResponseBody
  public String query(@PathVariable(value="server") String server, @PathVariable(value="character") String character) {
    return this.wowAchievementAdapter.queryAchievements(character, server);
  }


  public static void main(String[] args) throws Exception {
      SpringApplication.run(AppController.class, args);
  }
}
