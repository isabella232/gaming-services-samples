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
using UnityEngine.Events;

namespace Google.Maps.Demos.Zoinkies {

  /// <summary>
  /// The game victory view is shown when the game is won.
  /// It provides a way for the player to restart a new game.
  /// </summary>
  public class GameVictoryView : BaseView {

    /// <summary>
    /// Notifies other game components that we are starting a new game.
    /// </summary>
    public UnityEvent NewGame;
    /// <summary>
    /// Reference to the spaceship animator.
    /// </summary>
    public Animator SpaceShipAnimator;

    /// <summary>
    /// Shows the idle animation
    /// </summary>
    void OnEnable() {
      if (SpaceShipAnimator != null) {
        SpaceShipAnimator.enabled = true;
        SpaceShipAnimator.Play("SpaceShipIdle");
      }
    }

    /// <summary>
    /// Disables the idle animation
    /// </summary>
    void OnDisable() {
      if (SpaceShipAnimator != null) {
        SpaceShipAnimator.enabled = false;
      }
    }

    /// <summary>
    /// Triggers a full restart
    /// </summary>
    public void OnResetClicked() {
      // Trigger a full restart
      NewGame?.Invoke();
    }
  }
}
