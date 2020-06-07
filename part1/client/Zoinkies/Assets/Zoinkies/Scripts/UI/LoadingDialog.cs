
using System;
using UnityEngine;
using UnityEngine.Assertions;
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies {

    public class LoadingDialog : BaseView {
        public Image Spinner;

        public float RotationSpeed = 50f;

        private Action callback;

        void Start() {
            Assert.IsNotNull(Spinner);
        }

        // Init is called once per frame
        void Update() {
            // Rotate spinner if available
            Spinner.rectTransform.Rotate(Vector3.forward, -RotationSpeed * Time.deltaTime);

        }
    }
}
