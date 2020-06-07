package com.google.maps.gaming.zoinkies.models;

import java.time.Instant;
import java.util.HashMap;

public class WorldData {

  private HashMap<String, SpawnLocation> Locations;
  private String CurrentServerTime;

  public WorldData() {
    Locations = new HashMap<>();
    CurrentServerTime = Instant.now().toString();
  }

  public HashMap<String, SpawnLocation> getLocations() {
    return Locations;
  }

  public void setLocations(HashMap<String, SpawnLocation> locations) {
    this.Locations = locations;
  }

  public String getCurrentServerTime() {
    CurrentServerTime = Instant.now().toString();
    return CurrentServerTime;
  }

  public void setCurrentServerTime(String currentServerTime) {
    CurrentServerTime = currentServerTime;
  }
}
