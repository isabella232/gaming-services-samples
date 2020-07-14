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
   * Identifier of the opponent (ex: MINION).
   */
  private String opponentTypeId;

  /**
   * Getter for OpponentTypeId
   * @return
   */
  public String getOpponentTypeId() {
    return opponentTypeId;
  }

  /**
   * Setter for OpponentTypeId
   * @param opponentTypeId
   */
  public void setOpponentTypeId(String opponentTypeId) {
    this.opponentTypeId = opponentTypeId;
  }
  /**
   * Indicates who starts the battle: True-Player False-NPC.
   */
  private Boolean playerStarts;

  /**
   * Getter for PlayerStarts
   * @return
   */
  public Boolean getPlayerStarts() {
    return playerStarts;
  }

  /**
   * Setter for PlayerStarts
   * @param playerStarts
   */
  public void setPlayerStarts(Boolean playerStarts) {
    this.playerStarts = playerStarts;
  }
  /**
   * Provides the max energy level of the NPC.
   */
  private int energyLevel;

  /**
   * Getter for EnergyLevel
   * @return
   */
  public int getEnergyLevel() {
    return energyLevel;
  }

  /**
   * Setter for EnergyLevel
   * @param energyLevel
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
   * @return
   */
  public int getMaxAttackScoreBonus() {
    return maxAttackScoreBonus;
  }

  /**
   * Setter for MaxAttackScoreBonus
   * @param maxAttackScoreBonus
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
   * @return
   */
  public int getMaxDefenseScoreBonus() {
    return maxDefenseScoreBonus;
  }

  /**
   * Setter for MaxDefenseScoreBonus
   * @param maxDefenseScoreBonus
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
   * @return
   */
  public Duration getCooldown() {
    return cooldown;
  }

  /**
   * Setter for Cooldown
   * @param cooldown
   */
  public void setCooldown(Duration cooldown) {
    this.cooldown = cooldown;
  }

  /**
   * Implementation of ToString for testing purposes
   * @return
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("BattleData{" + "Id='" + this.locationId + '\'' + ", "
        + ""+ "PlayerStarts:" + this.playerStarts + ", "
        + ""+ "Cooldown:" + this.cooldown + ", "
        + ""+ "EnergyLevel:" + this.energyLevel + ", "
        + ""+ "MaxAttackScoreBonus:" + this.maxAttackScoreBonus + ", "
        + ""+ "MaxDefenseScoreBonus:" + this.maxDefenseScoreBonus + ", "
        + ""+ "OpponentTypeId:" + this.opponentTypeId);
    return sb.toString();
  }
}
