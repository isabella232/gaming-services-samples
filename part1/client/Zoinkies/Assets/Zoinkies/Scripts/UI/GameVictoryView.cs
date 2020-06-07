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
