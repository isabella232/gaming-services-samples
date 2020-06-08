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
using System;
using System.Linq;
using UnityEngine;

namespace Google.Maps.Demos.Zoinkies {

  public class TowerController : BaseSpawnLocationController {

    protected override void ActionState() {
      if (IsLoading)
        return;

      base.ActionState();

      if (string.IsNullOrEmpty(LocationId)) {
        Debug.LogError("Incorrect PlaceId!");
        return;
      }

      // Check if this station is active?
      // If not show a floating popup with timeout information
      location = WorldService.GetInstance().GetSpawnLocation(LocationId);

      if (WorldService.GetInstance().IsRespawning(LocationId)) {
        var t = DateTime.Parse(location.respawn_time);
        var timeLeft = t.Subtract(DateTime.Now);

        // Hide Minion... until it is respawned
        UIManager.OnShowLoadingView(false);

        UIManager.OnShowMessageDialog("Minion in transit", timeLeft);
        return;
      }

      // Check pre-requisites
      if (location.key_type_id == GameConstants.DIAMOND_KEY
          && PlayerService.GetInstance().GetNumberOfDiamondKeys() >=
          location.number_of_keys_to_activate) {

        // All good. Let's request our rewards
        Debug.Log("Pre reqs met!");

        try {
          IsLoading = true;
          StartCoroutine(ServerManager.PostBattleData(LocationId, OnBattleSuccess, OnError));
        }
        catch (System.Exception e) {
          Debug.LogError("Failed to free the leader in this tower! " + e);
          IsLoading = false;
          UIManager.OnShowLoadingView(false);
        }
      }
      else {
        Debug.Log("Pre reqs not met!");
        UIManager.OnShowMessageDialog("You need "
                                      + location.number_of_keys_to_activate
                                      + " Diamond Keys. \n You have "
                                      + PlayerService.GetInstance().GetNumberOfDiamondKeys()
                                      + " of them!");
        UIManager.OnShowLoadingView(false);
      }
    }

    private void OnBattleSuccess(BattleData data) {
      UIManager.OnShowLoadingView(false);
      IsLoading = false;
      if (data == null) return;
      // We've unlocked this location
      location.number_of_keys_to_activate = 0;

      // Start Battle!
      UIManager.OnShowBattleView(data, OnBattleEnds);
    }

    private void OnBattleEnds(bool Winner) {
      IsLoading = true;
      // Notify the server about battle results - Player->1  NPC->0
      StartCoroutine(
        ServerManager.PostBattleSummary(LocationId, Winner, OnBattleSummarySuccess, OnError));
    }

    private void OnBattleSummarySuccess(BattleSummaryData data) {
      IsLoading = false;
      if (data.winner) {
        // The tower is gone
        RespawingState();
        UIManager.OnShowLoadingView(false);
        if (data.wonTheGame)
          UIManager.OnShowGameVictoryView();
        else
          UIManager.OnShowLootResultsDialog("Victory!",data.rewards.items);
      }
      else {
        UIManager.OnShowLootResultsDialog("Defeat!",data.rewards.items);
      }
    }

    protected override SpawnLocation GetTestingLocation() {
      // Pick first available minion
      return WorldService.GetInstance().GetTowers().ElementAt(0);
    }
  }
}
