using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace Google.Maps.Demos.Zoinkies {

    public class FaceCamera : MonoBehaviour {
        // Always look at the camera
        void Update() {
            // Rotate around the (z) axis.
            if (Camera.main != null) {
                this.transform.LookAt(Camera.main.transform, Camera.main.transform.up);
                this.transform.rotation =
                    Quaternion.Euler(0, this.transform.rotation.eulerAngles.y, 0);
            }
        }
    }
}
