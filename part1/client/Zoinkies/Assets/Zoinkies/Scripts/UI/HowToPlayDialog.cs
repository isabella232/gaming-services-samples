
using UnityEngine;
using UnityEngine.Assertions;
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies {

    public class HowToPlayDialog : BaseView {
        public Button MapButton;
        public Button NextButton;

        void Start() {
            Assert.IsNotNull(MapButton);
            Assert.IsNotNull(NextButton);
        }

        void OnEnable() {
            bool IsFTUEComplete = PlayerPrefs.HasKey(GameConstants.FTUE_COMPLETE);
            MapButton.gameObject.SetActive(IsFTUEComplete);
            NextButton.gameObject.SetActive(!IsFTUEComplete);
        }
    }
}
