using System.Collections;
using System.Collections.Generic;
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
