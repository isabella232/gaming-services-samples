/**
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.maps.gaming.zoinkies.models;

import java.time.Instant;
import java.util.HashMap;

/**
 * A POJO class used to keep track of the world data and used in the worlds REST API.
 *
 */
public class WorldData {
  /**
   * Used to remember which S2Cells have been queried to playable locations API
   * and their TTL.
   * When the TTL has reached zero, we remove the entry from this cache so that it is queried again
   * next time.
   */
  private HashMap<String, String> s2CellsTTL;

  /**
   * Getter for S2 Cells Time to Live
   * @return
   */
  public HashMap<String, String> getS2CellsTTL() {
    return s2CellsTTL;
  }

  /**
   * Setter for S2 Cells Time to Live
   * @param s2CellsTTL
   */
  public void setS2CellsTTL(HashMap<String, String> s2CellsTTL) {
    this.s2CellsTTL = s2CellsTTL;
  }

  /**
   * A collection of locations identified by their location id (the unique name provided for each
   * playable location. Note that we aren't using PlaceId. Generated locations do not have PlaceIds.
   * They do have plusCodes instead.
   */
  private HashMap<String, SpawnLocation> locations;

  /**
   * Getter for locations map.
   * @return
   */
  public HashMap<String, SpawnLocation> getLocations() {
    return locations;
  }

  /**
   * Setter for locations map.
   * @param locations
   */
  public void setLocations(HashMap<String, SpawnLocation> locations) {
    this.locations = locations;
  }

  /**
   * A snapshot at the current server time. For future use. The current implementation is not
   * sync-ing server and client times. But it would be a requirement in a production environment.
   */
  private String currentServerTime;

  /**
   * Getter for current server time.
   * @return
   */
  public String getCurrentServerTime() {
    currentServerTime = Instant.now().toString();
    return currentServerTime;
  }

  /**
   * Setter for current server time.
   * @param currentServerTime
   */
  public void setCurrentServerTime(String currentServerTime) {
    this.currentServerTime = currentServerTime;
  }

  /**
   * Default constructor - instantiates empty maps.
   */
  public WorldData() {
    s2CellsTTL = new HashMap<>();
    locations = new HashMap<>();
    currentServerTime = Instant.now().toString();
  }
}
