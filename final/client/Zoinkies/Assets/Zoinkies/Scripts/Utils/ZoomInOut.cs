using UnityEngine;

namespace Google.Maps.Demos.Zoinkies {

    /// <summary>
    /// This class handles zoom in/zoom out while in the Editor
    /// </summary>
    public class ZoomInOut : MonoBehaviour {
        public Camera Camera;

        // Init is called once per frame
        void Update() {
            if (Input.GetKey(KeyCode.Q)) {
                // Zoom in
                Camera.fieldOfView += 1;
                Camera.fieldOfView = Mathf.Min(110f, Camera.fieldOfView);
            }

            if (Input.GetKey(KeyCode.E)) {
                // Zoom out
                Camera.fieldOfView -= 1;
                Camera.fieldOfView = Mathf.Max(25f, Camera.fieldOfView);
            }
        }
    }
}
