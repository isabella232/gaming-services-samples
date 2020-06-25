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
    public class ShakeScreen : MonoBehaviour
    {
        [SerializeField] private float duration = 1.0f; //how fast it shakes
        [SerializeField] private float magnitude = 1.0f; //how much it shakes

        private GameObject target; // object to shake

        public IEnumerator Shake(float duration, float magnitude)
        {
            if (target == null)
            {
                yield return null;
            }

            Vector3 orignalPosition = target.transform.position;
            float elapsed = 0f;

            while (elapsed < duration)
            {
                float x = orignalPosition.x + Random.Range(-1f, 1f) * magnitude;
                float y = orignalPosition.y + Random.Range(-1f, 1f) * magnitude;

                transform.position = new Vector3(x, y, 0f); //-10f);
                elapsed += Time.deltaTime;
                yield return 0;
            }

            transform.position = orignalPosition;
        }

        public void Shake(GameObject t, float d)
        {
            Debug.Log("Shake");
            duration = d;
            target = t;
            StartCoroutine(Shake(duration, magnitude));
        }
    }
}
