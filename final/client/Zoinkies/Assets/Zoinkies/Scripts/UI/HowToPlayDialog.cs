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

using UnityEngine;
using UnityEngine.Assertions;
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies
{
    public class HowToPlayDialog : BaseView
    {
        public Button MapButton;
        public Button NextButton;

        private void Start()
        {
            Assert.IsNotNull(MapButton);
            Assert.IsNotNull(NextButton);
        }

        private void OnEnable()
        {
            bool IsFTUEComplete = PlayerPrefs.HasKey(GameConstants.FTUE_COMPLETE);
            MapButton.gameObject.SetActive(IsFTUEComplete);
            NextButton.gameObject.SetActive(!IsFTUEComplete);
        }
    }
}
