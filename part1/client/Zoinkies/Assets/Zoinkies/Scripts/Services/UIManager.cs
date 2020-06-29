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
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    ///     This class handles all UI interaction and UI flow.
    /// </summary>
    public class UIManager : MonoBehaviour
    {
        /// <summary>
        ///     Event dispatched when a battle has concluded and we are reverting to the game world.
        /// </summary>
        public UnityEvent ShowWorld;

        /// <summary>
        ///     Reference to the character sheet.
        /// </summary>
        public CharacterSheetView CharacterSheetView;

        /// <summary>
        ///     Reference to the help screen.
        /// </summary>
        public HowToPlayDialog HowToPlayDialog;

        /// <summary>
        ///     Reference to the map HUD.
        /// </summary>
        public MapHUDView MapHudView;

        /// <summary>
        ///     Reference to the loading glass panel.
        /// </summary>
        public LoadingDialog LoadingDialog;

        /// <summary>
        ///     Reference to the message dialog.
        /// </summary>
        public MessageDialog MessageDialog;

        /// <summary>
        ///     Reference to the splash screen.
        /// </summary>
        public SplashView SplashView;

        /// <summary>
        ///     Used to report system messages
        /// </summary>
        public Text StatusMsg;

        /// <summary>
        ///     This flag (re)enables the FTUE (First time user experience) flow
        /// </summary>
        public bool EnableFTUE;

        /// <summary>
        ///     Provides a quick access to all screens in the game.
        /// </summary>
        private readonly List<BaseView> _views = new List<BaseView>();

        /// <summary>
        ///     Keeps track of the previous view
        /// </summary>
        private BaseView _currentView;

        private const float STATUS_MESSAGE_TIMEOUT = 3f;

        private float _statusMessageTimeCounter = 0f;

        private bool _showStatusMessage = false;


        /// <summary>
        ///     Initializes callbacks and initializes the splash screen.
        /// </summary>
        void Awake()
        {
            // When the debug flag is enabled, we run the FTUE at each start.
            if (EnableFTUE && PlayerPrefs.HasKey(GameConstants.FTUE_COMPLETE))
            {
                PlayerPrefs.DeleteKey(GameConstants.FTUE_COMPLETE);
            }

            // Register views
            _views.Add(CharacterSheetView);
            _views.Add(HowToPlayDialog);
            _views.Add(MapHudView);
            _views.Add(SplashView);
            _views.Add(LoadingDialog);
            _views.Add(MessageDialog);

            // Initializes all callbacks
            InitCallbacks();

            // Show first screen
            OnNewGame();
        }

        /// <summary>
        /// Display a status message for a limited amount of time.
        /// </summary>
        void Update()
        {
            if (_showStatusMessage)
            {
                _statusMessageTimeCounter += Time.deltaTime;
                if (_statusMessageTimeCounter > STATUS_MESSAGE_TIMEOUT)
                {
                    _statusMessageTimeCounter = 0f;
                    _showStatusMessage = false;
                    StatusMsg.text = "";
                }
            }

        }

        /// <summary>
        ///     Shows the Message dialog
        /// </summary>
        /// <param name="message">The message to display</param>
        public void OnShowMessageDialog(string message)
        {
            MessageDialog.Init(message);
            MessageDialog.gameObject.SetActive(true);
        }

        /// <summary>
        ///     Shows the Message dialog (Timer option)
        /// </summary>
        /// <param name="message">The message to display</param>
        /// <param name="timeLeft">The time left on a timer</param>
        public void OnShowMessageDialog(string message, TimeSpan timeLeft)
        {
            MessageDialog.Init(message, timeLeft);
            MessageDialog.gameObject.SetActive(true);
        }

        /// <summary>
        ///     Activates the loading screen. This screen behaves like a modal dialog.
        /// </summary>
        /// <param name="bool">A boolean to show or hide a view</param>
        public void OnShowLoadingView(bool show)
        {
            LoadingDialog.gameObject.SetActive(show);
        }

        /// <summary>
        ///     Activates the Character Sheet
        /// </summary>
        public void OnShowCharacterSheet()
        {
            ShowView(CharacterSheetView);
        }

        /// <summary>
        ///     Activates the Help screen
        /// </summary>
        public void OnShowHowToPlayDialog()
        {
            ShowView(HowToPlayDialog);
        }


        /// <summary>
        ///     Shows the Map
        /// </summary>
        public void OnShowMap()
        {
            ShowView(MapHudView);
        }

        /// <summary>
        ///     Shows the Map on escape (different flow)
        /// </summary>
        public void OnEscape()
        {
            ShowWorld?.Invoke();
            OnShowMap();
        }

        /// <summary>
        ///     Shows the Splash screen.
        /// </summary>
        public void OnNewGame()
        {
            _currentView = null;
            ShowView(SplashView);
        }

        /// <summary>
        /// Callback triggered when an error message is bubbled up to the UI by other
        /// game components.
        /// </summary>
        /// <param name="errorMsg">The error message></param>
        public void OnError(string errorMsg)
        {
            StatusMsg.text = errorMsg;
            _showStatusMessage = true;
            _statusMessageTimeCounter = 0f;
        }

        /// <summary>
        ///     Initializes a set of callbacks to set the game in World or Battle modes.
        /// </summary>
        private void InitCallbacks()
        {
            CharacterSheetView.OnClose += () => { ShowWorld?.Invoke(); };

            // Register callbacks
            SplashView.OnClose += () =>
            {
                _currentView = null;
                // If first time experience, show How to play
                // Otherwise show the map
                if (PlayerPrefs.HasKey(GameConstants.FTUE_COMPLETE))
                {
                    OnShowMap();
                }
                else
                {
                    OnShowHowToPlayDialog();
                }
            };
        }

        /// <summary>
        ///     Helper function to show the given view and hide all others.
        /// </summary>
        /// <param name="view">The view to show</param>
        /// <exception cref="Exception">Exception if the view is invalid</exception>
        private void ShowView(BaseView view)
        {
            StatusMsg.text = "";

            if (view == null)
            {
                throw new System.Exception("Invalid view object!");
            }

            if (_currentView != null)
            {
                _currentView.Close();
            }

            // Hide all other views
            foreach (BaseView v in _views)
            {
                v.gameObject.SetActive(v == view);
                if (v == view)
                {
                    _currentView = v;
                }
            }
        }
    }
}
