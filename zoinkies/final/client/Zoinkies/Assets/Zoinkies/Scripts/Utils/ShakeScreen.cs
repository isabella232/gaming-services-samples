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

using System.Collections;
using UnityEngine;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    /// Helper class to shake the screen when hit
    /// </summary>
    public class ShakeScreen : MonoBehaviour
    {
        /// <summary>
        /// How long it shakes
        /// </summary>
        public float Duration = 1.0f; //
        /// <summary>
        /// How intensive it shakes
        /// </summary>
        public float Magnitude = 1.0f;
        /// <summary>
        /// The object to shake
        /// </summary>
        private GameObject _target;

        /// <summary>
        /// Shakes the target object for the given duration and with the given magnitude
        /// </summary>
        /// <param name="duration">The duration of the shake</param>
        /// <param name="magnitude">The magnitude of the shake</param>
        /// <returns>An enumerator to a coroutine</returns>
        public IEnumerator Shake(float duration, float magnitude)
        {
            if (_target == null)
            {
                yield return null;
            }

            Vector3 transformPosition = _target.transform.position;
            float elapsed = 0f;

            while (elapsed < duration)
            {
                float x = transformPosition.x + Random.Range(-1f, 1f) * magnitude;
                float y = transformPosition.y + Random.Range(-1f, 1f) * magnitude;

                transform.position = new Vector3(x, y, 0f);
                elapsed += Time.deltaTime;
                yield return 0;
            }

            transform.position = transformPosition;
        }

        /// <summary>
        /// Starts the shake animation
        /// </summary>
        /// <param name="target"></param>
        /// <param name="duration"></param>
        public void Shake(GameObject target, float duration)
        {
            Duration = duration;
            _target = target;
            StartCoroutine(Shake(Duration, Magnitude));
        }
    }
}
