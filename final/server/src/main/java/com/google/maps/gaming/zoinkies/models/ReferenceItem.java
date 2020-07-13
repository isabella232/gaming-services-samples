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
  private String Id;

  /**
   * Getter for Id
   * @return
   */
  public String getId() {
    return Id;
  }

  /**
   * Setter for Id
   * @param id
   */
  public void setId(String id) {
    Id = id;
  }

  /**
   * Friendly name
   */
  private String Name;

  /**
   * Getter for name
   * @return
   */
  public String getName() {
    return Name;
  }

  /**
   * Setter for name
   * @param name
   */
  public void setName(String name) {
    Name = name;
  }

  /**
   * Object type
   */
  private String Type;

  /**
   * Getter for type
   * @return
   */
  public String getType() {
    return Type;
  }

  /**
   * Setter for type
   * @param type
   */
  public void setType(String type) {
    Type = type;
  }

  /**
   * Friendly description
   */
  private String Description;

  /**
   * Getter for description
   * @return
   */
  public String getDescription() {
    return Description;
  }

  /**
   * Setter for description
   * @param description
   */
  public void setDescription(String description) {
    Description = description;
  }

  /**
   * Base attack score
   */
  private int AttackScore;

  /**
   * Getter for attack score
   * @return
   */
  public int getAttackScore() {
    return AttackScore;
  }

  /**
   * Setter for attack score
   * @param attackScore
   */
  public void setAttackScore(int attackScore) {
    AttackScore = attackScore;
  }

  /**
   * Base defense score
   */
  private int DefenseScore;

  /**
   * Getter for defense score
   * @return
   */
  public int getDefenseScore() {
    return DefenseScore;
  }

  /**
   * Setter for defense score
   * @param defenseScore
   */
  public void setDefenseScore(int defenseScore) {
    DefenseScore = defenseScore;
  }

  /**
   * Base attack cooldown
   */
  private Duration Cooldown;

  /**
   * Getter for cooldown.
   * @return
   */
  @JsonFormat(shape = Shape.STRING)
  public Duration getCooldown() {
    return Cooldown;
  }

  /**
   * Setter for cooldown
   * @param cooldown
   */
  public void setCooldown(Duration cooldown) {
    Cooldown = cooldown;
  }
  /**
   * Respawn duration. No value indicates the object does not respawn.
   */
  private Duration RespawnDuration;

  /**
   * Getter for respawn duration
   * @return
   */
  @JsonFormat(shape = Shape.STRING)
  public Duration getRespawnDuration() {
    return RespawnDuration;
  }

  /**
   * Setter for respawn duration
   * @param respawnDuration
   */
  public void setRespawnDuration(Duration respawnDuration) {
    RespawnDuration = respawnDuration;
  }
  /**
   * Prefab name to be instantiated by the Unity client for this object.
   */
  private String Prefab;

  /**
   * Getter for prefab
   * @return
   */
  public String getPrefab() {
    return Prefab;
  }

  /**
   * Setter for prefab
   * @param prefab
   */
  public void setPrefab(String prefab) {
    Prefab = prefab;
  }

  /**
   * ToString override, mostly for testing purposes.
   * @return
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Id: " + Id);
    sb.append("Name: " + Name);
    sb.append("Type: " + Type);
    sb.append("Description: " + Description);
    sb.append("AttackScore: " + AttackScore);
    sb.append("DefenseScore: " + DefenseScore);
    sb.append("Cooldown: " + Cooldown);
    sb.append("RespawnDuration: " + RespawnDuration);
    sb.append("Prefab: " + Prefab);
    return sb.toString();
  }
}
