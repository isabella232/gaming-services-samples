
using System.Collections;
using UnityEngine;

namespace Google.Maps.Demos.Zoinkies {

    public class ShakeScreen : MonoBehaviour {
        [SerializeField] private float duration = 1.0f; //how fast it shakes
        [SerializeField] private float magnitude = 1.0f; //how much it shakes

        private GameObject target; // object to shake

        public IEnumerator Shake(float duration, float magnitude) {
            if (target == null)
                yield return null;

            Vector3 orignalPosition = target.transform.position;
            float elapsed = 0f;

            while (elapsed < duration) {
                float x = orignalPosition.x + Random.Range(-1f, 1f) * magnitude;
                float y = orignalPosition.y + Random.Range(-1f, 1f) * magnitude;

                transform.position = new Vector3(x, y, 0f); //-10f);
                elapsed += Time.deltaTime;
                yield return 0;
            }

            transform.position = orignalPosition;
        }

        public void Shake(GameObject t, float d) {
            Debug.Log("Shake");
            this.duration = d;
            this.target = t;
            StartCoroutine(Shake(duration, magnitude));
        }
    }
}
