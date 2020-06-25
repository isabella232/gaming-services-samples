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

using Unity.Collections;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies
{
    public class JoystickController : MonoBehaviour, IDragHandler, IPointerUpHandler,
        IPointerDownHandler
    {
      /// <summary>
      ///     On start, set joystick defaults.
      ///     If the app is deployed on mobile device, show the joystick in the scene,
      ///     and initialize the cameraRig.
      /// </summary>
      private void Start()
        {
            InputDirection = Vector3.zero;

            // If the example isn't run on mobile devices, hide the joystick
            //#if (UNITY_IOS || UNITY_ANDROID) && !UNITY_EDITOR
            gameObject.transform.parent.gameObject.SetActive(true);
            // Initialize the camera container's position
            //#endif
        }

      /// <summary>
      ///     On Updates, adjust the position and CW, CCW rotations of the Rig.
      ///     The code also applies vertical motions if Up and Down buttons are continously pressed.
      /// </summary>
      private void Update()
        {
            if (PlayerService.GetInstance().IsInitialized)
            {
                ReferenceItem refItem = ReferenceService.GetInstance()
                    .GetItem(PlayerService.GetInstance().AvatarType);
                if (refItem != null && refItem.id != currentAvatarType && AvatarImage != null)
                {
                    Texture2D t = (Texture2D) Resources.Load("Icon" + refItem.prefab);
                    Sprite s = Sprite.Create(t, AvatarImage.sprite.rect, AvatarImage.sprite.pivot);
                    AvatarImage.sprite = s;
                    currentAvatarType = refItem.id;
                }
            }

            // Move the camera at a speed that is linearly dependent on the height of the camera above
            // the ground plane to make camera manual camera movement practicable. The movement speed
            // is clamped between 1% and 100% of the configured MovementSpeed.
            float forwardSpeed = Mathf.Clamp(
                                     target.transform.position.y,
                                     MaxForwardSpeed * 0.1f,
                                     MaxForwardSpeed) * Time.deltaTime;


            if (InputDirection.magnitude != 0 && target != null)
            {
                float rotationDirection = 1f;
                float angle = Vector3.Angle(InputDirection, Vector3.right);

                if (angle > 90f)
                {
                    rotationDirection = -1f;
                }

                if (angle < 80f || angle > 100)
                {
                    // Rotate target around y axis
                    target.transform.RotateAround(
                        target.transform.position,
                        Vector3.up,
                        rotationDirection * MaxRotationSpeed * InputDirection.magnitude *
                        Time.deltaTime);
                }
                else
                {
                    float dir = InputDirection.y >= 0 ? 1f : -1f;
                    target.transform.position += target.transform.forward * forwardSpeed * dir
                                                 * InputDirection.magnitude;
                }
            }
        }

        #region properties

        public Image AvatarImage;
        [ReadOnly] public string currentAvatarType = "";

        // Joystick bounds
        public Image Background;

        // Camera Rig to move
        public Transform target;

        // Direction to follow
        public Vector3 InputDirection;

        // Lever knob to control forward/reverse, CW and CCW rotations
        public Image Lever;

        // Maximum rotation speed
        public float MaxRotationSpeed = 1f;

        // Maximum forward/reverse speed
        public float MaxForwardSpeed = 200f;

        #endregion

        #region event listeners

        /// <summary>
        ///     Implements the IDragHandler interface.
        ///     The function converts the drag of the joystick knob on the UI overlay
        ///     to a direction vector in worldspace that can be applied to our target.
        /// </summary>
        /// <param name="ped">The pointer event data</param>
        public void OnDrag(PointerEventData ped)
        {
            // Current position
            Vector2 pos = Vector2.zero;
            RectTransform rect = Background.rectTransform;

            // Move the target based on the Lever's position
            RectTransformUtility.ScreenPointToLocalPointInRectangle(
                rect,
                ped.position,
                ped.pressEventCamera,
                out pos);

            pos.x = pos.x / rect.sizeDelta.x;
            pos.y = pos.y / rect.sizeDelta.y;

            InputDirection = new Vector3(pos.x, pos.y, 0f);
            InputDirection = InputDirection.magnitude > 1
                ? InputDirection.normalized
                : InputDirection;

            Lever.rectTransform.anchoredPosition = new Vector3(
                InputDirection.x * (rect.sizeDelta.x / 3),
                InputDirection.y * rect.sizeDelta.y / 3);
        }

        /// <summary>
        ///     Implements the IPointerUpHandler interface.
        ///     Applies changes in similar ways to the OnDrag function.
        /// </summary>
        /// <param name="ped">The pointer event data</param>
        public void OnPointerDown(PointerEventData ped)
        {
            OnDrag(ped);
        }

        /// <summary>
        ///     Implements the IPointerDownHandler interface.
        ///     Resets the position of the joystick knob and the direction vector.
        /// </summary>
        /// <param name="ped"></param>
        public void OnPointerUp(PointerEventData ped)
        {
            Lever.rectTransform.anchoredPosition = Vector3.zero;
            InputDirection = Vector3.zero;
        }

        #endregion
    }
}
