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
