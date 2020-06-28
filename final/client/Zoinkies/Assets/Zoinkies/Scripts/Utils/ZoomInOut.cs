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
    ///     This class handles zoom in/zoom out while in the Editor
    /// </summary>
    public class ZoomInOut : MonoBehaviour
    {
        /// <summary>
        /// Reference to the active camera
        /// </summary>
        public Camera Camera;

        /// <summary>
        /// Updates the field of view when some keyboard keys are activated.
        /// </summary>
        void Update()
        {
            if (Input.GetKey(KeyCode.Q))
            {
                // Zoom in
                Camera.fieldOfView += 1;
                Camera.fieldOfView = Mathf.Min(110f, Camera.fieldOfView);
            }

            if (Input.GetKey(KeyCode.E))
            {
                // Zoom out
                Camera.fieldOfView -= 1;
                Camera.fieldOfView = Mathf.Max(25f, Camera.fieldOfView);
            }
        }
    }
}
