package com.google.maps.gaming.zoinkies.models;

import java.time.Instant;
import java.util.HashMap;

/**
 * A POJO class used to keep track of the world data and used in the worlds REST API.
 *
 */
public class WorldData {

  /**
   * A collection of locations identified by their location id (the unique name provided for each
   * playable location. Note that we aren't using PlaceId. Generated locations do not have PlaceIds.
   * They do have plusCodes instead.
   */
  private HashMap<String, SpawnLocation> Locations;
  /**
   * A snapshot at the current server time. For future use. The current implementation is not
   * sync-ing server and client times. But it would be a requirement in a production environment.
   */
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
