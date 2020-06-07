using System;
using System.Linq;
using UnityEngine;

namespace Google.Maps.Demos.Zoinkies {

    public class ZoinkiesMinionController : BaseSpawnLocationController //, IZoinkiesController
    {
        protected override void UpdateImpl() {
            base.UpdateImpl();

            if (Camera.main != null) {
                this.transform.LookAt(Camera.main.transform, Camera.main.transform.up);
                this.transform.rotation =
                    Quaternion.Euler(0, this.transform.rotation.eulerAngles.y, 0);
            }
        }

        protected override void RespawingState() {
            base.RespawingState();
            this.Model.SetActive(false);
        }

        protected override void ReadyState() {
            base.ReadyState();
            this.Model.SetActive(true);
        }

        protected override void ActionState() {
            if (IsLoading)
                return;

            base.ActionState();

            if (String.IsNullOrEmpty(PlaceId)) {
                Debug.LogError("Incorrect PlaceId!");
                UIManager.OnShowLoadingView(false);
                return;
            }

            // Check if this station is active?
            // If not show a floating popup with timeout information
            location = WorldService.GetInstance().GetSpawnLocation(PlaceId);

            if (WorldService.GetInstance().IsRespawning(PlaceId)) {
                DateTime t = DateTime.Parse(location.respawn_time);
                TimeSpan timeLeft = t.Subtract(DateTime.Now);

                // Hide Minion... until it is respawned
                UIManager.OnShowLoadingView(false);
                UIManager.OnShowMessageDialog("Minion in transit", timeLeft);
                return;
            }

            try {
                IsLoading = true;
                StartCoroutine(ServerManager.PostBattleData(PlaceId, OnBattleSuccess, OnError));
            }
            catch (System.Exception e) {
                Debug.LogError("Failed to open chest! " + e.ToString());
                IsLoading = false;
                UIManager.OnShowLoadingView(false);
            }
        }

        private void OnBattleSuccess(BattleData data) {

            UIManager.OnShowLoadingView(false);
            IsLoading = false;
            if (data == null) {
                return;
            }

            // Start Battle!
            UIManager.OnShowBattleView(data, OnBattleEnds);
        }

        private void OnBattleEnds(bool Winner) {

            IsLoading = true;
            UIManager.OnShowLoadingView(true);

            // Notify the server about battle results - Player->1  NPC->0
            StartCoroutine(ServerManager.PostBattleSummary(this.PlaceId, Winner,
                OnBattleSummarySuccess, OnError));
        }

        private void OnBattleSummarySuccess(BattleSummaryData data) {

            IsLoading = false;

            UIManager.OnShowLoadingView(false);

            // Start respawning
            WorldService.GetInstance().StartRespawn(PlaceId);

            if (data.winner) {
                if (data.wonTheGame) {
                    UIManager.OnShowGameVictoryView();
                }
                else {
                    UIManager.OnShowLootResultsDialog("Victory!",data.rewards.items);
                }
            }
            else {
                UIManager.OnShowLootResultsDialog("Defeat!",data.rewards.items);
            }

            // Hide this location until next respawn
            if (Model != null)
                Model.SetActive(false);

        }

        protected override SpawnLocation GetTestingLocation() {
            // Pick first available minion
            return WorldService.GetInstance().GetMinions().ElementAt(0);
        }
    }
}
