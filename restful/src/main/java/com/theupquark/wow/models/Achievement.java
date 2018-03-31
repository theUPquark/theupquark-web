package com.theupquark.wow.models;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Achievement {

  //milliseconds
  private String time;
  private String id;
  private List<Character> earnedBy;

  public Achievement() {
    this.time = "0";
    this.id = "0";
    this.earnedBy = new ArrayList<>();
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getTime() {
    return this.time;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return this.id;
  }

  public void setEarnedBy(List<Character> earnedBy) {
    this.earnedBy = earnedBy;
  }

  public List<Character> getEarnedBy() {
    return this.earnedBy;
  }

  public boolean equals(Achievement other) {
    if (other instanceof Achievement) {

      return this.timeTolerance(this.time, other.time)
        && Objects.equals(this.id, other.id);
    }
    return false;
  }

  public static boolean timeTolerance(String first, String second) {
    long difference = Long.parseLong(first) - Long.parseLong(second);
    if (Math.abs(difference) <= 60000) {
      return true;
    }
    return false;
  }
}
