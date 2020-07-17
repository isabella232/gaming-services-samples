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
using UnityEngine;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    /// Controller class for tower locations.
    ///
    /// Towers can only be accessible if players have the right elements in their inventories.
    /// When accessed, a call to the server initializes a boss battle, loads battle data,
    /// and switches the game into battle mode.
    ///
    /// When a battle is over, this controller triggers the battle summary sequence.
    ///
    /// Towers do not respawn. They are always available if the player meets the prerequisites.
    ///
    /// </summary>
    public class TowerController : BaseSpawnLocationController
    {
        /// <summary>
        /// Implementation of the action state.
        /// This function checks if all pre-requisites are met to interact with the tower.
        /// If all conditions are met, it triggers a call to the server to get battle details.
        /// </summary>
        protected override void ActionState()
        {
            if (IsLoading)
            {
                return;
            }

            base.ActionState();

            if (string.IsNullOrEmpty(LocationId))
            {
                Debug.LogError("Incorrect PlaceId!");
                return;
            }

            // Check if this station is active?
            // If not show a floating popup with timeout information
            location = WorldService.GetInstance().GetSpawnLocation(LocationId);

            // Check pre-requisites
            if (location.keyTypeId == GameConstants.DIAMOND_KEY
                && PlayerService.GetInstance().GetNumberOfDiamondKeys() >=
                location.numberOfKeysToActivate)
            {
                // All good. Let's request our rewards
                try
                {
                    IsLoading = true;
                    StartCoroutine(
                        ServerManager.PostBattleData(LocationId, OnBattleSuccess, OnError));
                }
                catch (System.Exception e)
                {
                    Debug.LogError("Failed to free the leader in this tower! " + e);
                    IsLoading = false;
                    UIManager.OnShowLoadingView(false);
                }
            }
            else
            {
                UIManager.OnShowMessageDialog("You need "
                                              + location.numberOfKeysToActivate
                                              + " Diamond Keys. \n You have "
                                              + PlayerService.GetInstance().GetNumberOfDiamondKeys()
                                              + " of them!");
                UIManager.OnShowLoadingView(false);
            }
        }

        /// <summary>
        /// Towers do not have a respawning state. They self-destroy after a successful victory.
        /// </summary>
        protected override void RespawingState()
        {
            // Self-destroy
            Destroy(gameObject);
        }

        /// <summary>
        /// Callback triggered when the request for battle parameters returns successfully.
        /// </summary>
        /// <param name="data">Battle Data</param>
        private void OnBattleSuccess(BattleData data)
        {
            UIManager.OnShowLoadingView(false);
            IsLoading = false;
            if (data == null)
            {
                return;
            }

            // We've unlocked this location
            location.numberOfKeysToActivate = 0;

            // Start Battle!
            UIManager.OnShowBattleView(data, OnBattleEnds);
        }

        /// <summary>
        /// Event listener triggered when the battle has ended with a victory or defeat.
        /// This wouldn't trigger if the player escapes.
        /// </summary>
        /// <param name="Winner"></param>
        private void OnBattleEnds(bool Winner)
        {
            IsLoading = true;
            // Notify the server about battle results - Player->1  NPC->0
            StartCoroutine(
                ServerManager.PostBattleSummary(LocationId, Winner, OnBattleSummarySuccess,
                    OnError));
        }

        /// <summary>
        /// Callback triggered when the server has acknowledge the battle results and returned
        /// a battle summary.
        /// </summary>
        /// <param name="data">Summary data</param>
        private void OnBattleSummarySuccess(BattleSummaryData data)
        {
            IsLoading = false;
            if (data.winner)
            {
                // The tower is gone
                RespawingState();
                UIManager.OnShowLoadingView(false);
                if (data.wonTheGame)
                {
                    UIManager.OnShowGameVictoryView();
                }
                else
                {
                    UIManager.OnShowLootResultsDialog("Victory!", data.rewards.items);
                }
            }
            else
            {
                UIManager.OnShowLootResultsDialog("Defeat!", data.rewards.items);
            }
        }
    }
}
