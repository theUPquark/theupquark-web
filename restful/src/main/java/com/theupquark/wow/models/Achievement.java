package com.theupquark.wow.models;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Achievement {

  //milliseconds
  private String time;
  private String id;
  private String title;
  private String points;
  private String description;

  public Achievement() {
    this.time = "0";
    this.id = "0";
    this.title = "";
    this.points = "";
    this.description = "";

  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return this.title;
  }

  public void setPoints(String points) {
    this.points = points;
  }

  public String getPoints() {
    return this.points;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
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

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Achievement) {
      Achievement other = (Achievement) obj;
      return this.timeTolerance(this.time, other.time)
        && Objects.equals(this.id, other.id);
    }
    return false;
  }

  /**
   * Difference in time to consider achievements occuring at the same time
   *
   * @param first time of first achievement
   * @param second time of other achievement
   * @return true if within time tolerance value
   */
  public static boolean timeTolerance(String first, String second) {
    long difference = Long.parseLong(first) - Long.parseLong(second);
    if (Math.abs(difference) <= 60000) {
      return true;
    }
    return false;
  }
}
