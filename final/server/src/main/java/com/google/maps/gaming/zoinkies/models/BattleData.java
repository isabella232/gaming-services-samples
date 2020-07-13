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
   * Identifier of the opponent (ex: MINION).
   */
  private String OpponentTypeId;

  /**
   * Getter for OpponentTypeId
   * @return
   */
  public String getOpponentTypeId() {
    return OpponentTypeId;
  }

  /**
   * Setter for OpponentTypeId
   * @param opponentTypeId
   */
  public void setOpponentTypeId(String opponentTypeId) {
    OpponentTypeId = opponentTypeId;
  }
  /**
   * Indicates who starts the battle: True-Player False-NPC.
   */
  private Boolean PlayerStarts;

  /**
   * Getter for PlayerStarts
   * @return
   */
  public Boolean getPlayerStarts() {
    return PlayerStarts;
  }

  /**
   * Setter for PlayerStarts
   * @param playerStarts
   */
  public void setPlayerStarts(Boolean playerStarts) {
    PlayerStarts = playerStarts;
  }
  /**
   * Provides the max energy level of the NPC.
   */
  private int EnergyLevel;

  /**
   * Getter for EnergyLevel
   * @return
   */
  public int getEnergyLevel() {
    return EnergyLevel;
  }

  /**
   * Setter for EnergyLevel
   * @param energyLevel
   */
  public void setEnergyLevel(int energyLevel) {
    this.EnergyLevel = energyLevel;
  }
  /**
   * Provides the bonus attack score of the NPC.
   */
  private int MaxAttackScoreBonus;

  /**
   * Getter for MaxAttackScoreBonus
   * @return
   */
  public int getMaxAttackScoreBonus() {
    return MaxAttackScoreBonus;
  }

  /**
   * Setter for MaxAttackScoreBonus
   * @param maxAttackScoreBonus
   */
  public void setMaxAttackScoreBonus(int maxAttackScoreBonus) {
    MaxAttackScoreBonus = maxAttackScoreBonus;
  }
  /**
   * Provides the bonus defense score of the NPC.
   */
  private int MaxDefenseScoreBonus;

  /**
   * Getter for MaxDefenseScoreBonus
   * @return
   */
  public int getMaxDefenseScoreBonus() {
    return MaxDefenseScoreBonus;
  }

  /**
   * Setter for MaxDefenseScoreBonus
   * @param maxDefenseScoreBonus
   */
  public void setMaxDefenseScoreBonus(int maxDefenseScoreBonus) {
    MaxDefenseScoreBonus = maxDefenseScoreBonus;
  }
  /**
   * Provides the cooldown value of the NPC during attacks.
   */
  private Duration Cooldown;

  /**
   * Getter for Cooldown
   * @return
   */
  public Duration getCooldown() {
    return Cooldown;
  }

  /**
   * Setter for Cooldown
   * @param cooldown
   */
  public void setCooldown(Duration cooldown) {
    Cooldown = cooldown;
  }

  /**
   * Implementation of ToString for testing purposes
   * @return
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("BattleData{" + "Id='" + this.Id + '\'' + ", "
        + ""+ "PlayerStarts:" + this.PlayerStarts + ", "
        + ""+ "Cooldown:" + this.Cooldown + ", "
        + ""+ "EnergyLevel:" + this.EnergyLevel + ", "
        + ""+ "MaxAttackScoreBonus:" + this.MaxAttackScoreBonus + ", "
        + ""+ "MaxDefenseScoreBonus:" + this.MaxDefenseScoreBonus + ", "
        + ""+ "OpponentTypeId:" + this.OpponentTypeId);
    return sb.toString();
  }
}
