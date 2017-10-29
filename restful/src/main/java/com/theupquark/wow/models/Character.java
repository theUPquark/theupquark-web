package com.theupquark.wow.models;


public class Character {

  private String name;
  private String server;

  public Character() {
    this.name = "";
    this.server = "";
  }

  public Character(String name, String server) {
    this.name = name;
    this.server = server;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public String getServer() {
    return this.server;
  }
}
