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
   * @return
   */
  public String getLocationId() {
    return locationId;
  }

  /**
   * Setter for Id
   * @param locationId
   */
  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }
  /**
   * Is the location currently active?
   */
  private Boolean active;

  /**
   * Getter for active.
   * @return
   */
  public Boolean getActive() {
    return active;
  }

  /**
   * Setter for active.
   * @param active
   */
  public void setActive(Boolean active) {
    this.active = active;
  }
  /**
   * Item representing this location: Tower, Minion, etc...
   */
  private String objectTypeId;

  /**
   * Getter for object type id
   * @return
   */
  public String getObjectTypeId() {
    return objectTypeId;
  }

  /**
   * Setter for object type id
   * @param objectTypeId
   */
  public void setObjectTypeId(String objectTypeId) {
    this.objectTypeId = objectTypeId;
  }
  /**
   * Is this location respawning?
   */
  private Boolean respawns;

  /**
   * Getter for respawns
   * @return
   */
  public Boolean getRespawns() {
    return respawns;
  }

  /**
   * Setter for respawns
   * @param respawns
   */
  public void setRespawns(Boolean respawns) {
    this.respawns = respawns;
  }
  /**
   * When will the location be active again?
   */
  private String respawnTime;

  /**
   * Getter for respawn time.
   * @return
   */
  public String getRespawnTime() {
    return respawnTime;
  }

  /**
   * Setter for respawn time.
   * @param respawnTime
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
   * @return
   */
  public int getNumberOfKeysToActivate() {
    return numberOfKeysToActivate;
  }

  /**
   * Setter for number of keys to activate
   * @param numberOfKeysToActivate
   */
  public void setNumberOfKeysToActivate(int numberOfKeysToActivate) {
    this.numberOfKeysToActivate = numberOfKeysToActivate;
  }
  /**
   * The type of "key" used to lock this location.
   * Keys can be any item in reference data.
   */
  private String keyTypeId;

  /**
   * Getter for key type id
   * @return
   */
  public String getKeyTypeId() {
    return keyTypeId;
  }

  /**
   * Setter for key type id
   * @param keyTypeId
   */
  public void setKeyTypeId(String keyTypeId) {
    this.keyTypeId = keyTypeId;
  }
  /**
   * The Latitude Location of this game location.
   */
  private LatLng snappedPoint;

  /**
   * Getter for snapped point
   * @return
   */
  public LatLng getSnappedPoint() {
    return snappedPoint;
  }

  /**
   * Setter for snapped point
   * @param snappedPoint
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
   * @return
   */
  public String getS2CellId() {
    return S2CellId;
  }

  /**
   * Setter for S2 Cell Id
   * @param s2CellId
   */
  public void setS2CellId(String s2CellId) {
    S2CellId = s2CellId;
  }

  public SpawnLocation() {}

  /**
   * Specific constructor.
   *
   * @param locationId
   * @param active
   * @param objectTypeId
   * @param respawns
   * @param respawnTime
   * @param numberOfKeysToActivate
   * @param keyTypeId
   * @param snappedPoint
   * @param s2CellId
   */
  public SpawnLocation(String locationId,
      Boolean active,
      String objectTypeId,
      Boolean respawns,
      String respawnTime,
      int numberOfKeysToActivate,
      String keyTypeId,
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
   * @return
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Id: " + locationId);
    sb.append("Active: " + active);
    sb.append("Object type id: " + objectTypeId);
    sb.append("Respawns: " + respawns);
    sb.append("Key type id: " + keyTypeId);
    sb.append("Respawn time: " + respawnTime);
    sb.append("Number of keys to activate: " + numberOfKeysToActivate);
    sb.append("Snapped Point: " + snappedPoint);
    sb.append("S2CellId: " + S2CellId);
    return sb.toString();
  }
}
