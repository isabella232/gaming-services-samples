using UnityEngine;

namespace Google.Maps.Demos.Zoinkies {

    /// <summary>
    /// This class repositions the camera behind the target (the Avatar in our example)
    /// with some delay identified by the damping parameters.
    /// </summary>
    public class SmoothFollowCamera : MonoBehaviour {
        /// <summary>
        /// The target we are following
        /// </summary>
        [Tooltip("The target we are following.")]
        public Transform target;
        /// <summary>
        /// The distance in the x-z plane to the target in meters.
        /// </summary>
        [Tooltip("The distance in the x-z plane to the target in meters.")]
        public float distance = 10.0f;
        /// <summary>
        /// The height we want the camera to be above the target
        /// </summary>
        [Tooltip("Our height in meters relative to the camera.")]
        public float height = 5.0f;
        /// <summary>
        /// The rotation speed to reposition the camera behind the target.
        /// </summary>
        [Tooltip("The rotation speed to reposition the camera behind the target.")]
        public float rotationDamping;
        /// <summary>
        /// The vertical speed to reposition the camera behind the target.
        /// </summary>
        [Tooltip("The vertical speed to reposition the camera behind the target.")]
        public float heightDamping;
        /// <summary>
        /// The reference to the main camera.
        /// </summary>
        [Tooltip("The reference to the main camera.")]
        public Camera cameraMain;

        /// <summary>
        /// Re-positions the camera behind our target.
        /// </summary>
        void LateUpdate() {
            // Early out if we don't have a target
            if (!target)
                return;

            // Calculate the current rotation angles
            var wantedRotationAngle = target.eulerAngles.y;
            var wantedHeight = target.position.y + height;

            var currentRotationAngle = cameraMain.transform.eulerAngles.y;
            var currentHeight = transform.position.y;

            // Damp the rotation around the y-axis
            currentRotationAngle =
                Mathf.LerpAngle(currentRotationAngle, wantedRotationAngle, rotationDamping * Time.deltaTime);

            // Damp the height
            currentHeight = Mathf.Lerp(currentHeight, wantedHeight, heightDamping * Time.deltaTime);

            // Convert the angle into a rotation
            var currentRotation = Quaternion.Euler(0, currentRotationAngle, 0);

            // Set the position of the camera on the x-z plane to:
            // distance meters behind the target
            transform.position = target.position;
            transform.position -= currentRotation * Vector3.forward * distance;

            // Set the height of the camera
            transform.position = new Vector3(transform.position.x, currentHeight, transform.position.z);

            // Always look at the target
            cameraMain.transform.LookAt(target);
        }
    }
}