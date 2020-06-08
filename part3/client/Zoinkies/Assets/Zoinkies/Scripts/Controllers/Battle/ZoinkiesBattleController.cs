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
