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
    ///     This class handles the firing animation of a laser beam during the battle sequence.
    /// </summary>
    public class AvatarBattleController : MonoBehaviour
    {
        /// <summary>
        ///     Keeps track of the laser beam scale. We use the scale property to "fire" the laser.
        /// </summary>
        private Vector3 _laserBeamScale;

        /// <summary>
        ///     Reference to the laser beam object.
        /// </summary>
        public Transform LaserBeam;

        /// <summary>
        ///     Reference to the target point of the laser beam.
        /// </summary>
        public Transform Target;

        /// <summary>
        ///     Reference to the origin point of the laser beam.
        /// </summary>
        public Transform Weapon;

        /// <summary>
        ///     Initializes the laser beam properties.
        /// </summary>
        private void Awake()
        {
            _laserBeamScale = LaserBeam.localScale;
        }

        /// <summary>
        ///     Called by the game, this function triggers the shooting animation sequence.
        /// </summary>
        /// <param name="missed"></param>
        public void OnShoot(bool missed = false)
        {
            StartCoroutine(Shoot(missed));
        }

        /// <summary>
        ///     Implements the shoot sequence.
        ///     If the attacker "misses", an offset is applied to the target point to indicate the miss.
        /// </summary>
        /// <param name="missed">
        ///     Indicates if this shooting action has reached or missed the target
        /// </param>
        /// <returns>An IEnumerator</returns>
        public IEnumerator Shoot(bool missed)
        {
            Vector3 point = Target.position;

            Vector3 randomDelta = Random.insideUnitSphere;

            if (missed)
            {
                point += Random.onUnitSphere * 10f;
            }

            randomDelta.z = 0f;

            point += randomDelta;
            Weapon.transform.LookAt(point);

            LaserBeam.localRotation = Weapon.transform.localRotation;

            float distance = Vector3.Distance(Weapon.transform.position, point);

            _laserBeamScale.z = distance;
            LaserBeam.localScale = _laserBeamScale;
            LaserBeam.localPosition = 0.5f * distance * LaserBeam.forward;

            yield return new WaitForSeconds(0.4f);

            _laserBeamScale.z = 0;
            LaserBeam.localScale = _laserBeamScale;
            LaserBeam.localPosition = Vector3.zero;
        }
    }
}
