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
using System.Collections.Generic;
using UnityEngine;

namespace Google.Maps.Demos.Zoinkies {

    public class ZoinkiesBattleController : MonoBehaviour {
        public Transform LaserBeam;
        public Transform Target;


        private Vector3 laserBeamScale;

        void Awake() {
            laserBeamScale = LaserBeam.localScale;
        }

        public void OnShoot(bool missed) {

            StartCoroutine(Shoot(missed));
        }

        public IEnumerator Shoot(bool missed) {

            Vector3 point = Target.position;

            if (missed) {
                point += Vector3.up * 2f;
            }

            this.transform.LookAt(point);
            transform.localRotation = this.transform.localRotation;

            float d = Vector3.Distance(this.transform.position, point);
            laserBeamScale.z = d;
            LaserBeam.localScale = laserBeamScale;
            LaserBeam.localPosition =
                0.5f * d * LaserBeam.forward;

            //LaserBeam.localPosition.y = 0f;
            //this.transform.localPosition +

            yield return new WaitForSeconds(0.4f);

            laserBeamScale.z = 0;
            LaserBeam.localScale = laserBeamScale;
            LaserBeam.localPosition = Vector3.zero;
        }
    }
}
