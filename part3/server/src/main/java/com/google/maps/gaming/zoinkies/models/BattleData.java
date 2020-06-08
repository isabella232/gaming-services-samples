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
