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

import com.google.maps.gaming.zoinkies.ITEMS;
import com.google.maps.gaming.zoinkies.models.playablelocations.LatLng;

/**
 * A POJO class to keep track of spawn locations details.
 */
public class SpawnLocation {
  /**
   * Location id
   */
  private String locationId;

  /**
   * Getter for Id
   * @return the location unique identifier
   */
  public String getLocationId() {
    return locationId;
  }

  /**
   * Setter for Id
   * @param locationId the location unique identifier
   */
  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }
  /**
   * Is the location currently active?
   */
  private boolean active;

  /**
   * Getter for active.
   * @return a boolean that indicates if the location is active
   */
  public Boolean getActive() {
    return active;
  }

  /**
   * Setter for active.
   * @param active a boolean that indicates if the location is active
   */
  public void setActive(Boolean active) {
    this.active = active;
  }
  /**
   * Item representing this location: Tower, Minion, etc...
   */
  private ITEMS objectTypeId;

  /**
   * Getter for object type id
   * @return the object type mapped to this location
   */
  public ITEMS getObjectTypeId() {
    return objectTypeId;
  }

  /**
   * Setter for object type id
   * @param objectTypeId the object type mapped to this location
   */
  public void setObjectTypeId(ITEMS objectTypeId) {
    this.objectTypeId = objectTypeId;
  }
  /**
   * Is this location respawning?
   */
  private boolean respawns;

  /**
   * Getter for respawns
   * @return a boolean to indicate if this location is respawning
   */
  public boolean getRespawns() {
    return respawns;
  }

  /**
   * Setter for respawns
   * @param respawns a boolean to indicate if this location is respawning
   */
  public void setRespawns(boolean respawns) {
    this.respawns = respawns;
  }
  /**
   * When will the location be active again?
   */
  private String respawnTime;

  /**
   * Getter for respawn time.
   * @return the time it takes to respawn this location
   */
  public String getRespawnTime() {
    return respawnTime;
  }

  /**
   * Setter for respawn time.
   * @param respawnTime the time it takes to respawn this location
   */
  public void setRespawnTime(String respawnTime) {
    this.respawnTime = respawnTime;
  }
  /**
   * If the location is locked, how many "keys" needed to activate it?
   * These keys must be owned by the player and will be consumed.
   */
  private int numberOfKeysToActivate;

  /**
   * Getter for number of keys to activate
   * @return the number of keys needed to activate the location
   */
  public int getNumberOfKeysToActivate() {
    return numberOfKeysToActivate;
  }

  /**
   * Setter for number of keys to activate
   * @param numberOfKeysToActivate the number of keys needed to activate the location
   */
  public void setNumberOfKeysToActivate(int numberOfKeysToActivate) {
    this.numberOfKeysToActivate = numberOfKeysToActivate;
  }
  /**
   * The type of "key" used to lock this location.
   * Keys can be any item in reference data.
   */
  private ITEMS keyTypeId;

  /**
   * Getter for key type id
   * @return the key type if
   */
  public ITEMS getKeyTypeId() {
    return keyTypeId;
  }

  /**
   * Setter for key type id
   * @param keyTypeId the key type id
   */
  public void setKeyTypeId(ITEMS keyTypeId) {
    this.keyTypeId = keyTypeId;
  }
  /**
   * The Latitude Location of this game location.
   */
  private LatLng snappedPoint;

  /**
   * Getter for snapped point
   * @return a lat lng for this location
   */
  public LatLng getSnappedPoint() {
    return snappedPoint;
  }

  /**
   * Setter for snapped point
   * @param snappedPoint the lat lng of the location
   */
  public void setSnappedPoint(
      LatLng snappedPoint) {
    this.snappedPoint = snappedPoint;
  }
  /**
   * The S2CellId from which this location was created.
   */
  private String S2CellId;

  /**
   * Getter for S2 Cell id
   * @return a string that contains the s2 cell id
   */
  public String getS2CellId() {
    return S2CellId;
  }

  /**
   * Setter for S2 Cell Id
   * @param s2CellId a string to map the s2 cell id
   */
  public void setS2CellId(String s2CellId) {
    S2CellId = s2CellId;
  }

  public SpawnLocation() {}

  /**
   * Specific constructor
   *
   * @param locationId the location id
   * @param active a boolean to indicate if the location is active
   * @param objectTypeId the object type associated to the location
   * @param respawns a boolean to indicate if the location is respawning
   * @param respawnTime the time it takes to respawn
   * @param numberOfKeysToActivate the number of keys to activate the location
   * @param keyTypeId the type of keys needed to activate the location
   * @param snappedPoint the latitude longitude coordinates of this location on the map
   * @param s2CellId the s2 cell id that this location belongs to
   */
  public SpawnLocation(String locationId,
      boolean active,
      ITEMS objectTypeId,
      boolean respawns,
      String respawnTime,
      int numberOfKeysToActivate,
      ITEMS keyTypeId,
      LatLng snappedPoint,
      String s2CellId) {
    this.locationId = locationId;
    this.active = active;
    this.objectTypeId = objectTypeId;
    this.respawns = respawns;
    this.keyTypeId = keyTypeId;
    this.respawnTime = respawnTime;
    this.numberOfKeysToActivate = numberOfKeysToActivate;
    this.snappedPoint = snappedPoint;
    this.S2CellId = s2CellId;
  }

  /**
   * ToString override, mostly for testing purposes.
   * @return a string description of this spawn location.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Id: ").append(locationId);
    sb.append("Active: ").append(active);
    sb.append("Object type id: ").append(objectTypeId);
    sb.append("Respawns: ").append(respawns);
    sb.append("Key type id: ").append(keyTypeId);
    sb.append("Respawn time: ").append(respawnTime);
    sb.append("Number of keys to activate: ").append(numberOfKeysToActivate);
    sb.append("Snapped Point: ").append(snappedPoint);
    sb.append("S2CellId: ").append(S2CellId);
    return sb.toString();
  }
}
