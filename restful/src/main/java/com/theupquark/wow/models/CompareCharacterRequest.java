package com.theupquark.wow.models;

import java.util.List;

/**
 * Contains information needed to make a request
 * to compare characters.
 */
public class CompareCharacterRequest {

  private List<Character> characters;
  private String region;
  private String locale;

  /**
   * Default constructor
   */
  public CompareCharacterRequest() { }

  public void setRegion(String region) {
    this.region = region.toLowerCase();
  }

  public String getRegion() {
    return this.region;
  }

  public void setCharacters(List<Character> characters) {
    this.characters = characters;
  }

  public List<Character> getCharacters() {
    return this.characters;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getLocale() {
    return this.locale;
  }
}
