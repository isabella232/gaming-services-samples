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
    ///     This class repositions the camera behind the target (the Avatar in our example)
    ///     with some delay identified by the damping parameters.
    /// </summary>
    public class SmoothFollowCamera : MonoBehaviour
    {
        /// <summary>
        ///     The reference to the main camera.
        /// </summary>
        [Tooltip("The reference to the main camera.")]
        public Camera cameraMain;

        /// <summary>
        ///     The distance in the x-z plane to the target in meters.
        /// </summary>
        [Tooltip("The distance in the x-z plane to the target in meters.")]
        public float distance = 10.0f;

        /// <summary>
        ///     The height we want the camera to be above the target
        /// </summary>
        [Tooltip("Our height in meters relative to the camera.")]
        public float height = 5.0f;

        /// <summary>
        ///     The vertical speed to reposition the camera behind the target.
        /// </summary>
        [Tooltip("The vertical speed to reposition the camera behind the target.")]
        public float heightDamping;

        /// <summary>
        ///     The rotation speed to reposition the camera behind the target.
        /// </summary>
        [Tooltip("The rotation speed to reposition the camera behind the target.")]
        public float rotationDamping;

        /// <summary>
        ///     The target we are following
        /// </summary>
        [Tooltip("The target we are following.")]
        public Transform target;

        /// <summary>
        ///     Re-positions the camera behind our target.
        /// </summary>
        private void LateUpdate()
        {
            // Early out if we don't have a target
            if (!target)
            {
                return;
            }

            // Calculate the current rotation angles
            float wantedRotationAngle = target.eulerAngles.y;
            float wantedHeight = target.position.y + height;

            float currentRotationAngle = cameraMain.transform.eulerAngles.y;
            float currentHeight = transform.position.y;

            // Damp the rotation around the y-axis
            currentRotationAngle =
                Mathf.LerpAngle(currentRotationAngle, wantedRotationAngle,
                    rotationDamping * Time.deltaTime);

            // Damp the height
            currentHeight = Mathf.Lerp(currentHeight, wantedHeight, heightDamping * Time.deltaTime);

            // Convert the angle into a rotation
            Quaternion currentRotation = Quaternion.Euler(0, currentRotationAngle, 0);

            // Set the position of the camera on the x-z plane to:
            // distance meters behind the target
            transform.position = target.position;
            transform.position -= currentRotation * Vector3.forward * distance;

            // Set the height of the camera
            transform.position =
                new Vector3(transform.position.x, currentHeight, transform.position.z);

            // Always look at the target
            cameraMain.transform.LookAt(target);
        }
    }
}
