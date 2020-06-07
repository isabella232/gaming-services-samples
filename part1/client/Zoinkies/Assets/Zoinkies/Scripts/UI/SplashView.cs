using System.Collections;
using System.Collections.Generic;
using Unity.Collections;
using UnityEngine;

namespace Google.Maps.Demos.Zoinkies {

    public class SplashView : BaseView {
        public Animator SpaceShipAnimator;

        // Start is called before the first frame update
        void OnEnable() {
            if (SpaceShipAnimator != null) {
                SpaceShipAnimator.enabled = true;
                //SpaceShipAnimator.Play("SpaceShipIdle");
            }
        }

        void OnDisable() {
            if (SpaceShipAnimator != null) {
                SpaceShipAnimator.enabled = false;
            }
        }

        public void OnGameReady() {
            // Close the view
            OnClose?.Invoke();
        }
    }
}
