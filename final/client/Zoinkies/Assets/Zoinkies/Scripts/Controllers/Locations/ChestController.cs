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
using System.Collections;
using System.Linq;
using UnityEngine;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    /// Controller class for chests game objects.
    /// </summary>
    public class ChestController : BaseSpawnLocationController
    {
        /// <summary>
        /// Reference to the animator used to play the open/close animations on the chest.
        /// </summary>
        public Animator Animator;

        /// <summary>
        /// Implementation of the Start function, triggered in the base class.
        /// </summary>
        protected override void StartImpl()
        {
            base.StartImpl();

            if (Animator != null)
            {
                // Close the chest for now
                Animator.SetBool("open", false);
                Animator.SetBool("reset", false);
            }
        }

        /// <summary>
        /// Implementation of the respawning state.
        /// After chests are opened, they respawn with a duration provided by the server.
        /// </summary>
        protected override void RespawingState()
        {
            // TODO Show lock and timer
        }

        /// <summary>
        /// Implementation of the Start function, triggered in the base class.
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


            // Check if this chest is active?
            // If not show a floating popup with timeout information
            location = WorldService.GetInstance().GetSpawnLocation(LocationId);

            if (WorldService.GetInstance().IsRespawning(LocationId))
            {
                UIManager.OnShowLoadingView(false);

                DateTime t = DateTime.Parse(location.respawn_time);
                TimeSpan timeLeft = t.Subtract(DateTime.Now);
                UIManager.OnShowMessageDialog("Chest locked. Time left: ", timeLeft);
                return;
            }

            // Check pre-requisites
            if (location.key_type_id == GameConstants.GOLD_KEY
                && PlayerService.GetInstance().GetNumberOfGoldKeys() >=
                location.number_of_keys_to_activate)
            {
                // All good. Let's request our rewards
                Debug.Log("Pre reqs met!");

                try
                {
                    IsLoading = true;
                    StartCoroutine(ServerManager.PostChest(LocationId, OnSuccess, OnError));
                }
                catch (System.Exception e)
                {
                    Debug.LogError("Failed to open chest! " + e);
                    IsLoading = false;
                    UIManager.OnShowLoadingView(false);
                }
            }
            else
            {
                UIManager.OnShowLoadingView(false);
                Debug.Log("Pre reqs not met!");
                UIManager.OnShowMessageDialog("You need "
                                              + location.number_of_keys_to_activate
                                              + " Gold Keys. \n You have "
                                              + PlayerService.GetInstance().GetNumberOfGoldKeys()
                                              + " of them!");
            }
        }

        /// <summary>
        /// Triggered when the server call to get the chest details returns successfully.
        /// </summary>
        /// <param name="data">Rewards data provided by the server.</param>
        private void OnSuccess(RewardsData data)
        {
            IsLoading = false;

            if (data == null)
            {
                // The chest is respawning
                return;
            }

            if (Animator != null)
            {
                // Open the chest for now
                Animator.SetBool("open", true);
                Animator.SetBool("reset", false);
            }

            StartCoroutine(CloseChest());

            // Preemptively update the player's data
            PlayerService.GetInstance().AddToInventory(data.items);
            // Preemptively start respawning
            WorldService.GetInstance().StartRespawn(LocationId);

            UIManager.OnShowLoadingView(false);

            if (UIManager != null)
            {
                UIManager.OnShowLootResultsDialog("Loot!", data.items);
            }
        }

        /// <summary>
        /// Plays the chest closing animation after a few seconds.
        /// </summary>
        /// <returns>An IEnumerator</returns>
        private IEnumerator CloseChest()
        {
            yield return new WaitForSeconds(3);

            if (Animator != null)
            {
                // Close the chest for now
                Animator.SetBool("open", false);
                Animator.SetBool("reset", true);
            }
        }
    }
}
