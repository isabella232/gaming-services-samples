package com.google.maps.gaming.zoinkies;

import java.time.Duration;

public class BattleData {
  private String PlaceId;
  private String OpponentTypeId;
  private Boolean PlayerStarts; // 1-Player 0-Opponent
  private int EnergyLevel;
  private int MaxAttackScoreBonus;
  private int MaxDefenseScoreBonus;
  private Duration Cooldown;

  public String getPlaceId() {
    return PlaceId;
  }

  public void setPlaceId(String placeId) {
    PlaceId = placeId;
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
    sb.append("BattleData{" + "PlaceId='" + this.PlaceId + '\'' + ", "
        + ""+ "PlayerStarts:" + this.PlayerStarts + ", "
        + ""+ "Cooldown:" + this.Cooldown + ", "
        + ""+ "EnergyLevel:" + this.EnergyLevel + ", "
        + ""+ "MaxAttackScoreBonus:" + this.MaxAttackScoreBonus + ", "
        + ""+ "MaxDefenseScoreBonus:" + this.MaxDefenseScoreBonus + ", "
        + ""+ "OpponentTypeId:" + this.OpponentTypeId);

    return sb.toString();
  }
}
