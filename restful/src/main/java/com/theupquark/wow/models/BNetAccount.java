package com.theupquark.wow.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BNetAccount contains information about a Battle Net account.
 * 
 * characterMap:
 *    key:    server
 *    value:  list of characters on server
 *
 * localization
 *    US, EU, etc..
 */
public class BNetAccount {


  private Map<String, List<String>> characterMap;
  private String localization;

  public BNetAccount() {
    this.characterMap = new HashMap<>();
    this.localization = "";
  }

  public void setCharacterMap(Map<String, List<String>> characterMap) {
    this.characterMap = characterMap;
  }

  public Map<String, List<String>> getCharacterMap() {
    return this.characterMap;
  }
}
