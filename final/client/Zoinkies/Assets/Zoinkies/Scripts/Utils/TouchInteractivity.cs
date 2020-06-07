
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.EventSystems;

namespace Google.Maps.Demos.Zoinkies {

    /// <summary>
    /// This class listens for cross platform player inputs, and triggers the OnClicked callback
    /// when the related gameobject is touched/clicked on.
    /// </summary>
    public class TouchInteractivity : MonoBehaviour {

        #region properties

        // What to do when the gameobject is touched/clicked
        public UnityEvent OnClicked;

        // Used for detecting hit on the gameobject
        private RaycastHit hit;
        private Ray ray;
        // Used to keep a reference on the camera - which might change as we load/unload the world scene
        public Camera activeCamera;

        /*
        /// <summary>
        /// The main camera will be destroyed when the scene is unloaded,
        /// and the gameobject which owns this component might be a persistent singleton.
        ///
        /// Make sure to reassign the camera if we lose the reference.
        ///
        /// </summary>
        /// <returns></returns>
        protected Camera GetCamera() {
            if (_activeCamera == null) {
                _activeCamera = Camera.main;
            }

            return _activeCamera;
        }
        */

        #endregion

        #region mechanics

        void Start() {
            activeCamera = Camera.main;
        }

        public void OnClickedEvent() {
            Debug.Log("Clicked on! ");
        }

        // Init is called once per frame
        void Update() {
            if (activeCamera != null) {

#if UNITY_EDITOR
                if (Input.GetMouseButtonDown(0) && !EventSystem.current.IsPointerOverGameObject()) {
                    ray = activeCamera.ScreenPointToRay(Input.mousePosition);
#endif

#if !UNITY_EDITOR && (UNITY_IOS || UNITY_ANDROID)
                // Detect a touch on the parent gameobject
                // If detected, trigger a OnButtonPressedImpl action

                if ((Input.touchCount > 0) && (Input.GetTouch(0).phase == TouchPhase.Began)
                    && !EventSystem.current.IsPointerOverGameObject(Input.touches[0].fingerId)) {
                    ray = activeCamera.ScreenPointToRay(Input.GetTouch(0).position);
#endif
                    if (Physics.Raycast(ray, out hit, 1000.0f)) {
                        if (hit.transform != null && hit.transform.gameObject == this.gameObject) {
                            if (OnClicked != null)
                                OnClicked.Invoke();
                        }
                    }
                }
            }
        }
        #endregion
    }
}
