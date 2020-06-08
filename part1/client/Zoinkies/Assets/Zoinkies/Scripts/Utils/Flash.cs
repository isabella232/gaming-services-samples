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

namespace Google.Maps.Demos.Zoinkies {

    public class Flash : MonoBehaviour {
        public Image Hit;
        public float flashSpeed = 3f;

        private bool IsHit = false;
        private float TIMEOUT = 4f;
        private float CurrentTimer = 0f;

        void Start() {
            Hit.color = Color.white;
            Hit.gameObject.SetActive(false);
            IsHit = false;
        }

        // Init is called once per frame
        void Update() {

            if (IsHit) {
                Hit.color = Color.Lerp(Hit.color, Color.clear, flashSpeed * Time.deltaTime);

                CurrentTimer += Time.deltaTime;
                if (CurrentTimer >= TIMEOUT) {
                    IsHit = false;
                    Hit.gameObject.SetActive(IsHit);
                }
            }
        }

        public void OnHit() {
            IsHit = true;
            Hit.gameObject.SetActive(IsHit);
            CurrentTimer = 0f;
            Hit.color = Color.white;
        }
    }
}
