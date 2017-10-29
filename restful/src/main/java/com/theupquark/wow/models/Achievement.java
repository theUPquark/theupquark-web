package com.theupquark.wow.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Achievement {

  //milliseconds
  private long time;
  private long id;
  private List<Character> earnedBy;

  public Achievement() {
    this.time = 0;
    this.id = 0;
    this.earnedBy = new ArrayList<>();
  }

  public void setTime(long time) {
    this.time = time;
  }

  public long getTime() {
    return this.time;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
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
      return Objects.equals(this.time, other.time)
        && Objects.equals(this.id, other.id);
    }
    return false;
  }
}
