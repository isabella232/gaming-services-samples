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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.Duration;

/**
 * A POJO class used to track reference items as loaded in the reference data list.
 */
public class ReferenceItem {

  /**
   * Unique identifier for this object
   */
  private String itemId;

  /**
   * Getter for Id
   * @return
   */
  public String getItemId() {
    return itemId;
  }

  /**
   * Setter for Id
   * @param itemId
   */
  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  /**
   * Friendly name
   */
  private String name;

  /**
   * Getter for name
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for name
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Object type
   */
  private String type;

  /**
   * Getter for type
   * @return
   */
  public String getType() {
    return type;
  }

  /**
   * Setter for type
   * @param type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Friendly description
   */
  private String description;

  /**
   * Getter for description
   * @return
   */
  public String getDescription() {
    return description;
  }

  /**
   * Setter for description
   * @param description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Base attack score
   */
  private int attackScore;

  /**
   * Getter for attack score
   * @return
   */
  public int getAttackScore() {
    return attackScore;
  }

  /**
   * Setter for attack score
   * @param attackScore
   */
  public void setAttackScore(int attackScore) {
    this.attackScore = attackScore;
  }

  /**
   * Base defense score
   */
  private int defenseScore;

  /**
   * Getter for defense score
   * @return
   */
  public int getDefenseScore() {
    return defenseScore;
  }

  /**
   * Setter for defense score
   * @param defenseScore
   */
  public void setDefenseScore(int defenseScore) {
    this.defenseScore = defenseScore;
  }

  /**
   * Base attack cooldown
   */
  private Duration cooldown;

  /**
   * Getter for cooldown.
   * @return
   */
  @JsonFormat(shape = Shape.STRING)
  public Duration getCooldown() {
    return cooldown;
  }

  /**
   * Setter for cooldown
   * @param cooldown
   */
  public void setCooldown(Duration cooldown) {
    this.cooldown = cooldown;
  }
  /**
   * Respawn duration. No value indicates the object does not respawn.
   */
  private Duration respawnDuration;

  /**
   * Getter for respawn duration
   * @return
   */
  @JsonFormat(shape = Shape.STRING)
  public Duration getRespawnDuration() {
    return respawnDuration;
  }

  /**
   * Setter for respawn duration
   * @param respawnDuration
   */
  public void setRespawnDuration(Duration respawnDuration) {
    this.respawnDuration = respawnDuration;
  }
  /**
   * Prefab name to be instantiated by the Unity client for this object.
   */
  private String prefab;

  /**
   * Getter for prefab
   * @return
   */
  public String getPrefab() {
    return prefab;
  }

  /**
   * Setter for prefab
   * @param prefab
   */
  public void setPrefab(String prefab) {
    this.prefab = prefab;
  }

  /**
   * ToString override, mostly for testing purposes.
   * @return
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Id: " + itemId);
    sb.append("Name: " + name);
    sb.append("Type: " + type);
    sb.append("Description: " + description);
    sb.append("AttackScore: " + attackScore);
    sb.append("DefenseScore: " + defenseScore);
    sb.append("Cooldown: " + cooldown);
    sb.append("RespawnDuration: " + respawnDuration);
    sb.append("Prefab: " + prefab);
    return sb.toString();
  }
}
