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
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    /// Simple class that creates a white flash for a few seconds.
    /// </summary>
    public class Flash : MonoBehaviour
    {
        /// <summary>
        /// Duration of the flash
        /// </summary>
        public float FlashSpeed = 3f;
        /// <summary>
        /// Reference to the Hit image
        /// </summary>
        public Image Hit;
        /// <summary>
        /// Triggers the actual hit animation
        /// </summary>
        private bool _isHit;
        /// <summary>
        /// Keeps track of the time count
        /// </summary>
        private float _timeCounter;
        /// <summary>
        /// Duration of a tick
        /// </summary>
        private const float TIMEOUT = 4f;

        /// <summary>
        /// Initializes the properties of the flash
        /// </summary>
        void Start()
        {
            Hit.color = Color.white;
            Hit.gameObject.SetActive(false);
            _isHit = false;
        }

        /// <summary>
        /// When hit, adjusts the color and the alpha of the hit image, at the given speed.
        /// </summary>
        void Update()
        {
            if (_isHit)
            {
                Hit.color = Color.Lerp(Hit.color, Color.clear, FlashSpeed * Time.deltaTime);

                _timeCounter += Time.deltaTime;
                if (_timeCounter >= TIMEOUT)
                {
                    _isHit = false;
                    Hit.gameObject.SetActive(_isHit);
                }
            }
        }

        /// <summary>
        /// When triggered, sets the animation in motion.
        /// </summary>
        public void OnHit()
        {
            _isHit = true;
            Hit.gameObject.SetActive(_isHit);
            _timeCounter = 0f;
            Hit.color = Color.white;
        }
    }
}
