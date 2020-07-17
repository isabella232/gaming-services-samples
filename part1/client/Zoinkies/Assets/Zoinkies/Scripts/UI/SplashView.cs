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
    /// This view handles the splash screen.
    /// It uses the main spaceship as an idle animation until all initial data is loaded
    /// and the game is ready to start.
    /// </summary>
    public class SplashView : BaseView
    {
        /// <summary>
        /// A reference to the spaceship animator
        /// </summary>
        public Animator SpaceShipAnimator;

        /// <summary>
        /// Plays the spaceship animation
        /// </summary>
        void OnEnable()
        {
            if (SpaceShipAnimator != null)
            {
                SpaceShipAnimator.enabled = true;
            }
        }

        /// <summary>
        /// Stops the spaceship animation
        /// </summary>
        void OnDisable()
        {
            if (SpaceShipAnimator != null)
            {
                SpaceShipAnimator.enabled = false;
            }
        }

        /// <summary>
        /// Triggered when a game ready event is received from other game components.
        /// Closes the splash screen when this happens.
        /// </summary>
        public void OnGameReady()
        {
            // Close the view
            OnClose?.Invoke();
        }
    }
}
