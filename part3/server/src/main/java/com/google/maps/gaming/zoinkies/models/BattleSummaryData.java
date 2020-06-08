package com.google.maps.gaming.zoinkies.models;

/**
 * A POJO class to handle the response from the Battle Summary REST API.
 */
public class BattleSummaryData {

  /**
   * Indicates who won. True-Player, False-NPC
   */
  private Boolean winner;
  /**
   * Provides a list of the rewards (or losses) that the player gets for winning/losing the battle.
   */
  private RewardsData rewards;
  /**
   * Indicates if the player has won the game (aka met the winning conditions).
   */
  private Boolean wonTheGame;

  public Boolean getWinner() {
    return winner;
  }

  public void setWinner(Boolean winner) {
    this.winner = winner;
  }

  public RewardsData getRewards() {
    return rewards;
  }

  public void setRewards(RewardsData rewards) {
    this.rewards = rewards;
  }

  public Boolean getWonTheGame() {
    return wonTheGame;
  }

  public void setWonTheGame(Boolean wonTheGame) {
    this.wonTheGame = wonTheGame;
  }
}
