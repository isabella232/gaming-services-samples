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

namespace Google.Maps.Demos.Zoinkies {

    public class MessageDialog : BaseView {
        public Text message;

        private string msg = "";
        private bool IsTime = false;
        private TimeSpan ts;
        private float TIMEOUT = 1f; // 1s
        private float ticker = 0f;

        // Start is called before the first frame update
        void Start() {
            Assert.IsNotNull(message);
        }

        void Update() {
            if (IsTime) {
                ticker += Time.deltaTime;
                if (ticker >= TIMEOUT) {
                    ts = ts.Subtract(TimeSpan.FromSeconds(1));
                    if (ts.Equals(TimeSpan.Zero)) {
                        IsTime = false;
                        message.text = "Done!";
                    }
                    else {
                        message.text = msg + ts.ToString(@"mm\:ss");
                    }

                    ticker = 0f;
                }
            }
        }

        public void Init(string msg) {
            if (!string.IsNullOrEmpty(msg)) {
                this.msg = msg;
                message.text = this.msg;
                IsTime = false;
                this.ts = TimeSpan.Zero;
            }
        }

        public void Init(string msg, TimeSpan ts) {
            if (ts != null && msg != null) {
                IsTime = true;
                this.ts = ts;
                this.msg = msg;
                message.text = msg + ts.ToString(@"mm\:ss");
            }
        }


        public void OnCloseButton() {
            // Close self - this is a dialog
            this.gameObject.SetActive(false);
        }
    }
}
