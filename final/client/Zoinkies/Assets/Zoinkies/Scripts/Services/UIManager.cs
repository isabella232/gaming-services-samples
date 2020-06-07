using System;
using System.Collections.Generic;
using Google.Maps.Demos.Utilities;
using UnityEngine;
using UnityEngine.Assertions;
using UnityEngine.Events;
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies {

    /// <summary>
    /// This class handles all UI interaction and UI flow.
    ///
    /// </summary>
    public class UIManager : MonoBehaviour {

        #region properties
        /// <summary>
        /// Event dispatched when a battle is about to start.
        /// </summary>
        public UnityEvent ShowBattleground;
        /// <summary>
        /// Event dispatched when a battle has concluded and we are reverting to the game world.
        /// </summary>
        public UnityEvent ShowWorld;
        /// <summary>
        /// Reference to the battle defeat dialog screen.
        /// </summary>
        public LootResultsDialog lootResultsDialog;
        /// <summary>
        /// Reference to the battle screen.
        /// </summary>
        public BattleView battleView;
        /// <summary>
        /// Reference to the character sheet.
        /// </summary>
        public CharacterSheetView CharacterSheetView;
        /// <summary>
        /// Reference to the game victory screen.
        /// </summary>
        public GameVictoryView GameVictoryView;
        /// <summary>
        /// Reference to the help screen.
        /// </summary>
        public HowToPlayDialog howToPlayDialog;
        /// <summary>
        /// Reference to the map HUD.
        /// </summary>
        public MapHUDView MapHudView;
        /// <summary>
        /// Reference to the loading glass panel.
        /// </summary>
        public LoadingDialog LoadingDialog;
        /// <summary>
        /// Reference to the message dialog.
        /// </summary>
        public MessageDialog MessageDialog;
        /// <summary>
        /// Reference to the splash screen.
        /// </summary>
        public SplashView SplashView;
        /// <summary>
        /// Used to report system messages
        /// </summary>
        public Text StatusMsg;
        /// <summary>
        /// This flag (re)enables the FTUE flow
        /// </summary>
        public bool EnableFTUE;
        /// <summary>
        /// Provides a quick access to all screens in the game.
        /// </summary>
        private List<BaseView> views = new List<BaseView>();
        /// <summary>
        /// Keeps track of the previous view
        /// </summary>
        private BaseView CurrentView;

        #endregion

        /// <summary>
        /// Initializes callbacks and initializes the splash screen.
        /// </summary>
        void Start() {

            // Debug
            if (EnableFTUE && PlayerPrefs.HasKey(GameConstants.FTUE_COMPLETE))
                PlayerPrefs.DeleteKey(GameConstants.FTUE_COMPLETE);

            // Register views
            views.Add(battleView);
            views.Add(CharacterSheetView);
            views.Add(GameVictoryView);
            views.Add(howToPlayDialog);
            views.Add(MapHudView);
            views.Add(SplashView);
            views.Add(lootResultsDialog);
            views.Add(LoadingDialog);
            views.Add(MessageDialog);

            // Initializes all callbacks
            InitCallbacks();

            // Show first screen
            OnNewGame();
        }

        /// <summary>
        /// Initializes a set of callbacks to set the game in World or Battle modes.
        /// </summary>
        private void InitCallbacks() {
            GameVictoryView.OnClose += () => { ShowWorld?.Invoke(); };
            //BattleVictoryDialog.OnClose += () => { ShowWorld?.Invoke(); };
            lootResultsDialog.OnClose += () => { ShowWorld?.Invoke(); };
            CharacterSheetView.OnClose += () => { ShowWorld?.Invoke(); };
            //ChestRewardsDialog.OnClose += () => { ShowWorld?.Invoke(); };

            // Register callbacks
            SplashView.OnClose += () => {
                CurrentView = null;
                // If first time experience, show How to play
                if (PlayerPrefs.HasKey(GameConstants.FTUE_COMPLETE)) {
                    OnShowMap();
                }
                else {
                    OnShowHowToPlayDialog();
                }
            };
        }

        #region event listeners

        /// <summary>
        /// Activates the Game Victory screen
        /// </summary>
        public void OnShowGameVictoryView() {
            ShowView(GameVictoryView);
        }

        /// <summary>
        /// Shows the Message dialog
        /// </summary>
        public void OnShowMessageDialog(String msg) {
            MessageDialog.Init(msg);
            MessageDialog.gameObject.SetActive(true);
        }

        /// <summary>
        /// Shows the Message dialog (Timer option)
        /// </summary>
        public void OnShowMessageDialog(String msg, TimeSpan ts) {
            MessageDialog.Init(msg, ts);
            MessageDialog.gameObject.SetActive(true);
        }

        /// <summary>
        /// Activates the Battle screen. This switches the game in Battle mode.
        /// </summary>
        public void OnShowBattleView(BattleData data, Action<bool> OnBattleEnds) {
            // Notify the game that we are switching from world to battleground
            ShowBattleground?.Invoke();
            battleView.Init(data, OnBattleEnds);
            ShowView(battleView);
        }

        /// <summary>
        /// Activates the loading screen. This screen behaves like a modal dialog.
        /// </summary>
        public void OnShowLoadingView(bool show) {
            LoadingDialog.gameObject.SetActive(show);
        }

        /// <summary>
        /// Activates the Battle defeat screen
        /// </summary>
        public void OnShowLootResultsDialog(string title, List<Item> items) { //BattleSummaryData data
            lootResultsDialog.Init(title, items);
            ShowView(lootResultsDialog);
        }

        /// <summary>
        /// Activates the Character Sheet
        /// </summary>
        public void OnShowCharacterSheet() {
            ShowView(CharacterSheetView);
        }

        /// <summary>
        /// Activates the Help screen
        /// </summary>
        public void OnShowHowToPlayDialog() {
            ShowView(howToPlayDialog);
        }


        /// <summary>
        /// Shows the Map
        /// </summary>
        public void OnShowMap() {
            ShowView(MapHudView);
        }

        /// <summary>
        /// Shows the Map on escape (different flow)
        /// </summary>
        public void OnEscape() {
            this.ShowWorld?.Invoke();
            OnShowMap();
        }

        /// <summary>
        /// Shows the Splash screen.
        /// </summary>
        public void OnNewGame() {
            CurrentView = null;
            ShowView(SplashView);
        }

        public void OnError(string errorMsg) {
            StatusMsg.text = errorMsg;
        }

        #endregion

        #region helper functions

        /// <summary>
        /// Helper function to show the given view and hide all others.
        /// </summary>
        /// <param name="view"></param>
        /// <exception cref="Exception"></exception>
        private void ShowView(BaseView view) {

            StatusMsg.text = "";

            if (view == null) {
                throw new System.Exception("Invalid view gameobject!");
            }

            if (CurrentView != null) {
                CurrentView.OnClose?.Invoke();
            }

            // Hide all other views
            foreach (BaseView v in views) {
                v.gameObject.SetActive(v == view);
                if (v == view) {
                    CurrentView = v;
                }
            }
        }

        #endregion
    }
}
