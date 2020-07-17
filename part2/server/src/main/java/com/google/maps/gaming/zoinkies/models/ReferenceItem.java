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
import com.google.maps.gaming.zoinkies.ITEMS;
import java.time.Duration;

/**
 * A POJO class used to track reference items as loaded in the reference data list.
 */
public class ReferenceItem {

  /**
   * Unique identifier for this object
   */
  private ITEMS itemId;

  /**
   * Getter for Id
   * @return the item unique id
   */
  public ITEMS getItemId() {
    return itemId;
  }

  /**
   * Setter for Id
   * @param itemId the item unique id
   */
  public void setItemId(ITEMS itemId) {
    this.itemId = itemId;
  }

  /**
   * Friendly name
   */
  private String name;

  /**
   * Getter for name
   * @return the item friendly name
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for name
   * @param name the item friendly name
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
   * @return the item type
   */
  public String getType() {
    return type;
  }

  /**
   * Setter for type
   * @param type the item type
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
   * @return the description for this item
   */
  public String getDescription() {
    return description;
  }

  /**
   * Setter for description
   * @param description the description for this item
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
   * @return the attack score for this item
   */
  public int getAttackScore() {
    return attackScore;
  }

  /**
   * Setter for attack score
   * @param attackScore the attack score for this item
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
   * @return the defense score for this item
   */
  public int getDefenseScore() {
    return defenseScore;
  }

  /**
   * Setter for defense score
   * @param defenseScore the defense score for this item
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
   * @return the cooldown for this item
   */
  @JsonFormat(shape = Shape.STRING)
  public Duration getCooldown() {
    return cooldown;
  }

  /**
   * Setter for cooldown
   * @param cooldown the cooldown for this item
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
   * @return the respawn duration
   */
  @JsonFormat(shape = Shape.STRING)
  public Duration getRespawnDuration() {
    return respawnDuration;
  }

  /**
   * Setter for respawn duration
   * @param respawnDuration the respawn duration
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
   * @return the prefab to instantiate on the client
   */
  public String getPrefab() {
    return prefab;
  }

  /**
   * Setter for prefab
   * @param prefab the prefab to instantiate on the client
   */
  public void setPrefab(String prefab) {
    this.prefab = prefab;
  }

  /**
   * ToString override, mostly for testing purposes.
   * @return a string that describes this reference item instance.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Id: ").append(itemId);
    sb.append("Name: ").append(name);
    sb.append("Type: ").append(type);
    sb.append("Description: ").append(description);
    sb.append("AttackScore: ").append(attackScore);
    sb.append("DefenseScore: ").append(defenseScore);
    sb.append("Cooldown: ").append(cooldown);
    sb.append("RespawnDuration: ").append(respawnDuration);
    sb.append("Prefab: ").append(prefab);
    return sb.toString();
  }
}
