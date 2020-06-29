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
using UnityEngine.Assertions;
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    /// This class handles the loading animation.
    /// When active, it displays a glass panel that blocks events to the UI.
    /// </summary>
    public class LoadingDialog : BaseView
    {
        /// <summary>
        /// The rotation speed of the busy visual
        /// </summary>
        public float RotationSpeed = 50f;

        /// <summary>
        /// A reference to the busy visual
        /// </summary>
        public Image Spinner;

        /// <summary>
        /// Checks the validity of the attributes
        /// </summary>
        void Start()
        {
            Assert.IsNotNull(Spinner);
        }

        /// <summary>
        /// Rotates the spinner
        /// </summary>
        void Update()
        {
            // Rotate spinner if available
            Spinner.rectTransform.Rotate(Vector3.forward, -RotationSpeed * Time.deltaTime);
        }
    }
}
