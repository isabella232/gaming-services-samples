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
using Unity.Collections;
using UnityEngine;
using UnityEngine.Assertions;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    /// Base class for all spawn locations.
    /// The controller provides hooks for basic states:
    ///   - Ready
    ///   - Action
    ///   - Respawning
    /// The sequence is as follows:
    /// Action <=> Respawning => Ready
    /// Some locations do not respawn, and only use two states:
    /// Ready <=> Action
    /// </summary>
    public class BaseSpawnLocationController : MonoBehaviour
    {
        /// <summary>
        /// Reference to the server manager.
        /// </summary>
        [ReadOnly] public ServerManager ServerManager;
        /// <summary>
        /// Reference to the UI manager.
        /// </summary>
        [ReadOnly] public UIManager UIManager;
         /// <summary>
        /// Reference to the actual mesh of this spawn location.
        /// When some locations respawn, they become invisible, until the ready conditions are met.
        /// </summary>
        public GameObject Model;
        /// <summary>
        /// Indicates if we have a pending server request.
        /// </summary>
        protected bool IsLoading;
        /// <summary>
        /// Reference to the spawn location data
        /// </summary>
        protected SpawnLocation location;
        /// <summary>
        /// The unique id for this location.
        /// </summary>
        protected string LocationId;

        void Start()
        {
            StartImpl();
        }

        void Update()
        {
            UpdateImpl();
        }

        /// <summary>
        /// Initializes this location with the information associated to the provided location id
        /// </summary>
        /// <param name="locationId"></param>
        public void Init(string locationId)
        {
            LocationId = locationId;
            location = WorldService.GetInstance().GetSpawnLocation(locationId);

            UIManager = GameObject.FindGameObjectWithTag("UIManager").GetComponent<UIManager>();
            ServerManager = GameObject.FindGameObjectWithTag("ServerManager")
                .GetComponent<ServerManager>();
        }

        /// <summary>
        /// Triggered when the location is touched.
        /// This function also detects how far the location is from the avatar.
        /// </summary>
        /// <remarks>
        /// Only objects within reach can be interacted with.
        /// The current maximum distance is arbitrarily set to 250 meters.
        /// </remarks>
        public void OnClicked()
        {
            //
            // We need to be within range
            GameObject avatar = GameObject.FindWithTag("Player");
            if (avatar != null)
            {
                float dist = Vector3.Distance(avatar.transform.position, transform.position);
                if (dist < 250)
                {
                    UIManager.OnShowLoadingView(true);
                    ActionState();
                }
                else
                {
                    UIManager.OnShowMessageDialog("This location is too far away. " +
                                                  "\nYou need to be within 250 meters! \nYou are "
                                                  + dist.ToString("N0") + " meters away.");
                }
            }
        }

        /// <summary>
        /// Actual implementation at Start that can be derived and overriden.
        /// </summary>
        protected virtual void StartImpl()
        {
            Assert.IsNotNull(Model);
        }

        /// <summary>
        /// Actual implementation at Update that can be derived and overriden.
        /// </summary>
        protected virtual void UpdateImpl()
        {
            if (LocationId != null && WorldService.GetInstance().IsRespawning(LocationId))
            {
                RespawingState();
            }
        }

        /// <summary>
        /// Base class implementation of the respawning state.
        /// </summary>
        protected virtual void RespawingState()
        {
        }

        /// <summary>
        /// Base class implementation of the ready state.
        /// </summary>
        protected virtual void ReadyState()
        {
        }

        /// <summary>
        /// Base class implementation of the action state.
        /// </summary>
        protected virtual void ActionState()
        {
        }

        /// <summary>
        /// Base class implementation for server errors.
        /// </summary>
        /// <param name="error">The error message string</param>
        protected virtual void OnError(string error)
        {
            Debug.LogError(error);
            IsLoading = false;
        }
    }
}
