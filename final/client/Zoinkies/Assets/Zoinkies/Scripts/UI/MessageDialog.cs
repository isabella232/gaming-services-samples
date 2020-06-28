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

using System;
using UnityEngine;
using UnityEngine.Assertions;
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    /// This view represents a simple message dialog.
    /// It has an option for actively displaying timers.
    /// </summary>
    public class MessageDialog : BaseView
    {
        /// <summary>
        /// A reference to the message widget
        /// </summary>
        public Text Message;

        /// <summary>
        /// Is the timer option active?
        /// </summary>
        private bool _hasTimer;

        /// <summary>
        /// Keeps track of the original message
        /// </summary>
        private string _message = "";

        /// <summary>
        /// Keeps track of time count
        /// </summary>
        private float _ticker;

        /// <summary>
        /// The duration of one tick (default is 1s)
        /// </summary>
        private const float TIMEOUT = 1f;

        /// <summary>
        /// The time left on the timer
        /// </summary>
        private TimeSpan _duration;

        /// <summary>
        /// Checks that all properties are valid.
        /// </summary>
        void Start()
        {
            Assert.IsNotNull(Message);
        }

        /// <summary>
        /// Updates the timer when one is provided.
        /// </summary>
        void Update()
        {
            if (_hasTimer)
            {
                _ticker += Time.deltaTime;
                if (_ticker >= TIMEOUT)
                {
                    _duration = _duration.Subtract(TimeSpan.FromSeconds(1));
                    if (_duration.Equals(TimeSpan.Zero))
                    {
                        _hasTimer = false;
                        Message.text = "Done!";
                    }
                    else
                    {
                        Message.text = _message + _duration.ToString(@"mm\:ss");
                    }

                    _ticker = 0f;
                }
            }
        }

        /// <summary>
        /// Initializes the dialog with a time
        /// </summary>
        /// <param name="message"></param>
        public void Init(string message)
        {
            if (!string.IsNullOrEmpty(message))
            {
                _message = message;
                Message.text = _message;
                _hasTimer = false;
                _duration = TimeSpan.Zero;
            }
        }

        /// <summary>
        /// Initializes the dialog with a message and a time left.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="duration"></param>
        public void Init(string message, TimeSpan duration)
        {
            if (duration != null && message != null)
            {
                _hasTimer = true;
                _duration = duration;
                _message = message;
                Message.text = message + duration.ToString(@"mm\:ss");
            }
        }

        /// <summary>
        /// Triggered when the close button is touched.
        /// </summary>
        public void OnCloseButton()
        {
            // Closes self - this is a dialog box
            gameObject.SetActive(false);
        }
    }
}
