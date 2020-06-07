using System;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;

namespace Google.Maps.Demos.Zoinkies {

    public class EnergyStationController : BaseSpawnLocationController {

        protected override void ActionState() {

            if (IsLoading)
                return;

            base.ActionState();

            // Testing - pick first energy station from world
            List<SpawnLocation> s =
                new List<SpawnLocation>(WorldService.GetInstance().GetEnergyStations());
            PlaceId = s.ElementAt(0).id;

            if (String.IsNullOrEmpty(PlaceId)) {
                Debug.LogError("Incorrect PlaceId!");
                UIManager.OnShowLoadingView(false);
                return;
            }

            // Check if this station is active?
            // If not show a floating popup with timeout information
            if (WorldService.GetInstance().IsRespawning(PlaceId)) {

                DateTime t = DateTime.Parse(location.respawn_time);
                TimeSpan timeLeft = t.Subtract(DateTime.Now);
                UIManager.OnShowMessageDialog("Station recharging. Time left: ", timeLeft);
                UIManager.OnShowLoadingView(false);
                return;
            }

            try {
                IsLoading = true;
                StartCoroutine(ServerManager.PostRechargingStation(PlaceId, OnSuccess, OnError));
            }
            catch (System.Exception e) {
                Debug.LogError("Failed to open chest! " + e.ToString());
                IsLoading = false;
                UIManager.OnShowLoadingView(false);
            }
        }

        private void OnSuccess(EnergyData data) {
            IsLoading = false;
            if (data == null) {
                // The station is recharging
                return;
            }

            WorldService.GetInstance().StartRespawn(PlaceId);

            // Init the player's data
            PlayerService.GetInstance().IncreaseEnergyLevel(data.amountRestored);

            UIManager.OnShowLoadingView(false);

            UIManager.OnShowMessageDialog("Energy recharged to 100%!");
        }

        protected override SpawnLocation GetTestingLocation() {
            // Pick first available minion
            return WorldService.GetInstance().GetEnergyStations().ElementAt(0);
        }
    }
}
