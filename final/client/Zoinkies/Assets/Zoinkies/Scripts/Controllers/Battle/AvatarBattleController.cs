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
