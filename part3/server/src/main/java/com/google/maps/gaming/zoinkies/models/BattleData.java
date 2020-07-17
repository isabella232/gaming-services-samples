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
import java.time.Duration;

/**
 * A POJO class to handle the response from the Battle REST API.
 */
public class BattleData {
  /**
   * Unique identifier for the battle location.
   */
  private String locationId;

  /**
   * Getter for Id
   * @return a location id
   */
  public String getLocationId() {
    return locationId;
  }

  /**
   * Setter for Id
   * @param locationId a location id
   */
  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }

  /**
   * Identifier of the opponent (ex: MINION).
   */
  private ITEMS opponentTypeId;

  /**
   * Getter for OpponentTypeId
   * @return the opponent type id
   */
  public ITEMS getOpponentTypeId() {
    return opponentTypeId;
  }

  /**
   * Setter for OpponentTypeId
   * @param opponentTypeId the opponent type id
   */
  public void setOpponentTypeId(ITEMS opponentTypeId) {
    this.opponentTypeId = opponentTypeId;
  }
  /**
   * Indicates who starts the battle: True-Player False-NPC.
   */
  private boolean playerStarts;

  /**
   * Getter for PlayerStarts
   * @return a boolean that indicates who starts the battle.
   */
  public boolean getPlayerStarts() {
    return playerStarts;
  }

  /**
   * Setter for PlayerStarts
   * @param playerStarts a boolean that indicates who starts the battle.
   */
  public void setPlayerStarts(boolean playerStarts) {
    this.playerStarts = playerStarts;
  }
  /**
   * Provides the max energy level of the NPC.
   */
  private int energyLevel;

  /**
   * Getter for EnergyLevel
   * @return the energy level of the NPC
   */
  public int getEnergyLevel() {
    return energyLevel;
  }

  /**
   * Setter for EnergyLevel
   * @param energyLevel the energy level of the NPC
   */
  public void setEnergyLevel(int energyLevel) {
    this.energyLevel = energyLevel;
  }
  /**
   * Provides the bonus attack score of the NPC.
   */
  private int maxAttackScoreBonus;

  /**
   * Getter for MaxAttackScoreBonus
   * @return the maximum attack score bonus of the NPC
   */
  public int getMaxAttackScoreBonus() {
    return maxAttackScoreBonus;
  }

  /**
   * Setter for MaxAttackScoreBonus
   * @param maxAttackScoreBonus the maximum attack score bonus of the NPC
   */
  public void setMaxAttackScoreBonus(int maxAttackScoreBonus) {
    this.maxAttackScoreBonus = maxAttackScoreBonus;
  }
  /**
   * Provides the bonus defense score of the NPC.
   */
  private int maxDefenseScoreBonus;

  /**
   * Getter for MaxDefenseScoreBonus
   * @return the maximum defense score bonus of the NPC
   */
  public int getMaxDefenseScoreBonus() {
    return maxDefenseScoreBonus;
  }

  /**
   * Setter for MaxDefenseScoreBonus
   * @param maxDefenseScoreBonus the maximum defense score bonus of the NPC
   */
  public void setMaxDefenseScoreBonus(int maxDefenseScoreBonus) {
    this.maxDefenseScoreBonus = maxDefenseScoreBonus;
  }
  /**
   * Provides the cooldown value of the NPC during attacks.
   */
  private Duration cooldown;

  /**
   * Getter for Cooldown
   * @return the cooldown of the NPC attack
   */
  public Duration getCooldown() {
    return cooldown;
  }

  /**
   * Setter for Cooldown
   * @param cooldown the cooldown of the NPC attack
   */
  public void setCooldown(Duration cooldown) {
    this.cooldown = cooldown;
  }

  /**
   * Implementation of ToString for testing purposes.
   * @return a string that describes this battle data instance.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("BattleData{" + "Id='").append(this.locationId).append('\'').append(", ")
        .append("PlayerStarts:").append(this.playerStarts).append(", ").append("Cooldown:")
        .append(this.cooldown).append(", ").append("EnergyLevel:").append(this.energyLevel)
        .append(", ").append("MaxAttackScoreBonus:").append(this.maxAttackScoreBonus).append(", ")
        .append("MaxDefenseScoreBonus:").append(this.maxDefenseScoreBonus).append(", ")
        .append("OpponentTypeId:").append(this.opponentTypeId);
    return sb.toString();
  }
}
