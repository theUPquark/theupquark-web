package com.theupquark.wow.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Main object used to send information to the API
 *
 * users:
 *  BNetAccount list
 */
public class WebAppSettings {

  private List<BNetAccount> users;

  public WebAppSettings() {
    this.users = new ArrayList<>();
  }

  public void setUsers(List<BNetAccount> users) {
    this.users = users;
  }

  public List<BNetAccount> getUsers() {
    return this.users;
  }
}
