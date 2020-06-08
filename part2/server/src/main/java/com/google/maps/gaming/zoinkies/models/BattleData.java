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
   * Identifier of the opponent (ex: MINION).
   */
  private String OpponentTypeId;
  /**
   * Indicates who starts the battle: True-Player False-NPC.
   */
  private Boolean PlayerStarts;
  /**
   * Provides the max energy level of the NPC.
   */
  private int EnergyLevel;
  /**
   * Provides the bonus attack score of the NPC.
   */
  private int MaxAttackScoreBonus;
  /**
   * Provides the bonus defense score of the NPC.
   */
  private int MaxDefenseScoreBonus;
  /**
   * Provides the cooldown value of the NPC during attacks.
   */
  private Duration Cooldown;

  public String getId() {
    return Id;
  }

  public void setId(String id) {
    Id = id;
  }

  public Boolean getPlayerStarts() {
    return PlayerStarts;
  }

  public void setPlayerStarts(Boolean playerStarts) {
    PlayerStarts = playerStarts;
  }

  public Duration getCooldown() {
    return Cooldown;
  }

  public void setCooldown(Duration cooldown) {
    Cooldown = cooldown;
  }

  public int getEnergyLevel() {
    return EnergyLevel;
  }

  public void setEnergyLevel(int energyLevel) {
    this.EnergyLevel = energyLevel;
  }

  public int getMaxAttackScoreBonus() {
    return MaxAttackScoreBonus;
  }

  public void setMaxAttackScoreBonus(int maxAttackScoreBonus) {
    MaxAttackScoreBonus = maxAttackScoreBonus;
  }

  public int getMaxDefenseScoreBonus() {
    return MaxDefenseScoreBonus;
  }

  public void setMaxDefenseScoreBonus(int maxDefenseScoreBonus) {
    MaxDefenseScoreBonus = maxDefenseScoreBonus;
  }

  public String getOpponentTypeId() {
    return OpponentTypeId;
  }

  public void setOpponentTypeId(String opponentTypeId) {
    OpponentTypeId = opponentTypeId;
  }

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
