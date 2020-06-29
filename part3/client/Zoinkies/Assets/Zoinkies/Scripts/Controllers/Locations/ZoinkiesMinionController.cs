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

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    /// This controller class handles a minion battle.
    /// It responds to the touch event and sets the battle initialization.
    ///
    /// Minions are valid targets when they aren't respawning.
    /// When accessed, a call to the server initializes a minion battle, loads battle data,
    /// and switches the game into battle mode.
    ///
    /// When the battle is over, the controller triggers the battle summary sequence.
    ///
    /// Minions always respawn. They are hidden from the map until respawn is done.
    /// </summary>
    public class ZoinkiesMinionController : BaseSpawnLocationController
    {
        /// <summary>
        /// Implementation of the Update function.
        /// Always rotate the minion to face the camera.
        /// </summary>
        protected override void UpdateImpl()
        {
            base.UpdateImpl();

            if (Camera.main != null)
            {
                transform.LookAt(Camera.main.transform, Camera.main.transform.up);
                transform.rotation =
                    Quaternion.Euler(0, transform.rotation.eulerAngles.y, 0);
            }
        }

        /// <summary>
        /// During respawning, the minion (mesh) is hidden from the map.
        /// </summary>
        protected override void RespawingState()
        {
            base.RespawingState();
            Model.SetActive(false);
        }

        /// <summary>
        /// When the minion has respawned and is now in ready state, the minion is shown on the map.
        /// </summary>
        protected override void ReadyState()
        {
            base.ReadyState();
            Model.SetActive(true);
        }

        /// <summary>
        /// When the minion is clicked on, we start a new battle if the conditions are met.
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
                UIManager.OnShowLoadingView(false);
                return;
            }

            // Check if this station is active?
            // If not show a floating popup with timeout information
            location = WorldService.GetInstance().GetSpawnLocation(LocationId);

            if (WorldService.GetInstance().IsRespawning(LocationId))
            {
                DateTime t = DateTime.Parse(location.respawn_time);
                TimeSpan timeLeft = t.Subtract(DateTime.Now);

                // Hide Minion... until it is respawned
                UIManager.OnShowLoadingView(false);
                UIManager.OnShowMessageDialog("Minion in transit", timeLeft);
                return;
            }

            try
            {
                IsLoading = true;
                StartCoroutine(ServerManager.PostBattleData(LocationId, OnBattleSuccess, OnError));
            }
            catch (System.Exception e)
            {
                Debug.LogError("Failed to get battle data! " + e);
                IsLoading = false;
                UIManager.OnShowLoadingView(false);
            }
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
            UIManager.OnShowLoadingView(true);

            // Notify the server about battle results - Player->1  NPC->0
            StartCoroutine(ServerManager.PostBattleSummary(LocationId, Winner,
                OnBattleSummarySuccess, OnError));
        }

        /// <summary>
        /// Callback triggered when the server has acknowledge the battle results and returned
        /// a battle summary.
        /// </summary>
        /// <param name="data">Summary data</param>
        private void OnBattleSummarySuccess(BattleSummaryData data)
        {
            IsLoading = false;

            UIManager.OnShowLoadingView(false);

            // Start respawning
            WorldService.GetInstance().StartRespawn(LocationId);

            if (data.winner)
            {
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

            // Hide this location until next respawn
            if (Model != null)
            {
                Model.SetActive(false);
            }
        }
    }
}
