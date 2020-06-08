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

namespace Google.Maps.Demos.Zoinkies {
  public class AvatarBattleController : MonoBehaviour {
    public Transform LaserBeam;
    public Transform Target;
    public Transform Weapon;

    private Vector3 laserBeamScale;

    void Awake() {
      laserBeamScale = LaserBeam.localScale;
    }


    public void OnShoot(bool missed = false) {

      StartCoroutine(Shoot(missed));
    }

    public IEnumerator Shoot(bool missed) {

      Vector3 point = Target.position;

      Vector3 randomDelta = Random.insideUnitSphere;

      if (missed) {
        point += Random.onUnitSphere * 10f;
      }

      randomDelta.z = 0f;

      point += randomDelta;
      Weapon.transform.LookAt(point);

      LaserBeam.localRotation = Weapon.transform.localRotation;

      float d = Vector3.Distance(Weapon.transform.position, point);
      laserBeamScale.z = d;
      LaserBeam.localScale = laserBeamScale;
      LaserBeam.localPosition =
        0.5f * d * LaserBeam.forward;

      yield return new WaitForSeconds(0.4f);

      laserBeamScale.z = 0;
      LaserBeam.localScale = laserBeamScale;
      LaserBeam.localPosition = Vector3.zero;
    }
  }
}
