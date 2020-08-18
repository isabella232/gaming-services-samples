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
    /// A helper script to turn objects so that the same side always faces the camera.
    /// </summary>
    public class FaceCamera : MonoBehaviour
    {
        /// <summary>
        /// Rotate the object to always look at the camera
        /// </summary>
        void Update()
        {
            // Rotate around the (z) axis.
            if (Camera.main != null)
            {
                transform.LookAt(Camera.main.transform, Camera.main.transform.up);
                transform.rotation =
                    Quaternion.Euler(0, transform.rotation.eulerAngles.y, 0);
            }
        }
    }
}
