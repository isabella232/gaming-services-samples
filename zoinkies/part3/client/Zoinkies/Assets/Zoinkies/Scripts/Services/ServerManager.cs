﻿/**
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
using System.Collections;
using System.Text;
using LitJson;
using UnityEngine;
using UnityEngine.Networking;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    ///     Server manager that makes all REST API calls to the game server.
    /// </summary>
    public class ServerManager : MonoBehaviour
    {
        /// <summary>
        /// The  server url used when testing locally
        /// </summary>
        private const string SERVER_URL = "http://localhost:8080";

        /// <summary>
        ///     Sends battle results to the server.
        ///     The server will compute the rewards (or the penalty) depending on who wins.
        /// </summary>
        /// <param name="locationId">The location id</param>
        /// <param name="Winner">The winner of the battle</param>
        /// <param name="onSuccess">The callback invoked when the call returns successfully</param>
        /// <param name="onError">The callback invoked if the call fails</param>
        /// <returns>An enumerator for the coroutine</returns>
        /// <exception cref="Exception">Exception when location id is invalid</exception>
        public IEnumerator PostBattleSummary(
            string locationId,
            bool Winner,
            Action<BattleSummaryData> onSuccess,
            Action<string> onError)
        {
            if (string.IsNullOrEmpty(locationId))
            {
                throw new System.Exception("Invalid location Id!");
            }

            using (UnityWebRequest webRequest =
                new UnityWebRequest(
                    SERVER_URL + "/battlesummary/" + GetUserId() + "/"
                    + locationId + "?winner=" + Winner,
                    "POST"))
            {
                webRequest.downloadHandler = new DownloadHandlerBuffer();
                webRequest.SetRequestHeader("Content-Type", "application/json");

                yield return webRequest.SendWebRequest();

                if (webRequest.isNetworkError || webRequest.isHttpError)
                {
                    onError(webRequest.error);
                }
                else
                {
                    if (webRequest.responseCode != 204)
                    {
                        onSuccess(
                            JsonMapper.ToObject<BattleSummaryData>(webRequest.downloadHandler
                                .text));
                    }
                    else
                    {
                        // Station recharging
                        onSuccess(null);
                    }
                }
            }
        }

        /// <summary>
        ///     Creates or updates a new battle on the server.
        ///     The data returned describes the parameter for the encounter.
        /// </summary>
        /// <param name="locationId"></param>
        /// <param name="onSuccess">The callback invoked when the call returns successfully</param>
        /// <param name="onError">The callback invoked if the call fails</param>
        /// <returns>An enumerator for the coroutine</returns>
        /// <exception cref="Exception">Exception when location id is invalid</exception>
        public IEnumerator PostBattleData(
            string locationId,
            Action<BattleData> onSuccess,
            Action<string> onError)
        {
            if (string.IsNullOrEmpty(locationId))
            {
                throw new System.Exception("Invalid Location Id!");
            }

            using (UnityWebRequest webRequest =
                new UnityWebRequest(SERVER_URL + "/battle/" + GetUserId() + "/"
                                    + locationId, "POST"))
            {
                webRequest.downloadHandler = new DownloadHandlerBuffer();
                webRequest.SetRequestHeader("Content-Type", "application/json");

                yield return webRequest.SendWebRequest();

                if (webRequest.isNetworkError || webRequest.isHttpError)
                {
                    onError(webRequest.error);
                }
                else
                {
                    if (webRequest.responseCode != 204)
                    {
                        onSuccess(JsonMapper.ToObject<BattleData>(webRequest.downloadHandler.text));
                    }
                    else
                    {
                        // Station recharging
                        onSuccess(null);
                    }
                }
            }
        }

        /// <summary>
        ///     Requests an energy recharge for our avatar to the server.
        ///     The data returned indicates how much energy was restored.
        /// </summary>
        /// <param name="locationId"></param>
        /// <param name="onSuccess">The callback invoked when the call returns successfully</param>
        /// <param name="onError">The callback invoked if the call fails</param>
        /// <returns>An enumerator for the coroutine</returns>
        /// <exception cref="Exception">Exception when location id is invalid</exception>
        public IEnumerator PostRechargingStation(
            string locationId,
            Action<EnergyData> onSuccess,
            Action<string> onError)
        {
            if (string.IsNullOrEmpty(locationId))
            {
                throw new System.Exception("Invalid Location Id!");
            }

            string json = "";

            using (UnityWebRequest webRequest =
                new UnityWebRequest(SERVER_URL + "/energystation/" + GetUserId()
                                    + "/" + locationId,
                    "POST"))
            {
                if (!string.IsNullOrEmpty(json))
                {
                    byte[] bodyRaw = Encoding.UTF8.GetBytes(json);
                    webRequest.uploadHandler = new UploadHandlerRaw(bodyRaw);
                }

                webRequest.downloadHandler = new DownloadHandlerBuffer();
                webRequest.SetRequestHeader("Content-Type", "application/json");

                yield return webRequest.SendWebRequest();

                if (webRequest.isNetworkError || webRequest.isHttpError)
                {
                    onError(webRequest.error);
                }
                else
                {
                    if (webRequest.responseCode != 204)
                    {
                        EnergyData data =
                            JsonMapper.ToObject<EnergyData>(webRequest.downloadHandler.text);
                        onSuccess(data);
                    }
                    else
                    {
                        // Station recharging
                        onSuccess(null);
                    }
                }
            }
        }

        /// <summary>
        ///     Notifies the server that the player is opening a chest.
        ///     Returns the content of the chest if this action is validated by the server.
        /// </summary>
        /// <param name="locationId"></param>
        /// <param name="onSuccess">The callback invoked when the call returns successfully</param>
        /// <param name="onError">The callback invoked if the call fails</param>
        /// <returns>An enumerator for the coroutine</returns>
        /// <exception cref="Exception">Exception when location id is invalid</exception>
        public IEnumerator PostChest(
            string locationId,
            Action<RewardsData> onSuccess,
            Action<string> onError)
        {
            if (string.IsNullOrEmpty(locationId))
            {
                throw new System.Exception("Invalid Location Id!");
            }

            string json = "";

            using (UnityWebRequest webRequest =
                new UnityWebRequest(SERVER_URL + "/chests/" + GetUserId() + "/"
                                    + locationId, "POST"))
            {
                if (!string.IsNullOrEmpty(json))
                {
                    byte[] bodyRaw = Encoding.UTF8.GetBytes(json);
                    webRequest.uploadHandler = new UploadHandlerRaw(bodyRaw);
                }

                webRequest.downloadHandler = new DownloadHandlerBuffer();
                webRequest.SetRequestHeader("Content-Type", "application/json");

                yield return webRequest.SendWebRequest();

                if (webRequest.isNetworkError || webRequest.isHttpError)
                {
                    onError(webRequest.error);
                }
                else
                {
                    if (webRequest.responseCode != 204)
                    {
                        onSuccess(
                            JsonMapper.ToObject<RewardsData>(webRequest.downloadHandler.text));
                    }
                    else
                    {
                        // Chest respawning
                        onSuccess(null);
                    }
                }
            }
        }


        /// <summary>
        ///     Loads reference data from the game server.
        ///     This data is now accessible with the ReferenceService.
        /// </summary>
        /// <param name="onSuccess">The callback invoked when the call returns successfully</param>
        /// <param name="onError">The callback invoked if the call fails</param>
        /// <returns>An enumerator for the coroutine</returns>
        public IEnumerator GetReferenceData(
            Action<ReferenceData> onSuccess,
            Action<string> onError)
        {
            using (UnityWebRequest webRequest = UnityWebRequest.Get(SERVER_URL + "/references"))
            {
                // Request and wait for the desired page.
                yield return webRequest.SendWebRequest();

                if (webRequest.isNetworkError || webRequest.isHttpError)
                {
                    onError(webRequest.error);
                }
                else
                {
                    // Show results as text
                    onSuccess?.Invoke(
                        JsonMapper.ToObject<ReferenceData>(webRequest.downloadHandler.text));
                }
            }
        }

        /// <summary>
        ///     Loads stats and inventory for the given player.
        /// </summary>
        /// <param name="onSuccess">The callback invoked when the call returns successfully</param>
        /// <param name="onError">The callback invoked if the call fails</param>
        /// <returns>An enumerator for the coroutine</returns>
        public IEnumerator GetPlayerData(
            Action<PlayerData> onSuccess,
            Action<string> onError)
        {
            using (UnityWebRequest webRequest =
                UnityWebRequest.Get(SERVER_URL + "/users/" + GetUserId())
            )
            {
                // Request and wait for the desired page.
                yield return webRequest.SendWebRequest();

                if (webRequest.isNetworkError || webRequest.isHttpError)
                {
                    onError(webRequest.error);
                }
                else
                {
                    // No record
                    if (webRequest.responseCode != 204)
                    {
                        // Show results as text
                        string json = webRequest.downloadHandler.text;
                        onSuccess?.Invoke(JsonMapper.ToObject<PlayerData>(json));
                    }
                    else
                    {
                        // Create it
                        StartCoroutine(PostPlayerData(null, onSuccess, onError));
                    }
                }
            }
        }

        /// <summary>
        ///     Creates (stats and inventory) or updates (stats) of the current player.
        ///     Note that the inventory is managed by the game server.
        /// </summary>
        /// <param name="data">A player data</param>
        /// <param name="onSuccess">The callback invoked when the call returns successfully</param>
        /// <param name="onError">The callback invoked if the call fails</param>
        /// <returns>An enumerator for the coroutine</returns>
        public IEnumerator PostPlayerData(
            PlayerData data,
            Action<PlayerData> onSuccess,
            Action<string> onError)
        {
            string json = "";
            if (data != null)
            {
                json = JsonMapper.ToJson(data);
            }

            using (UnityWebRequest webRequest =
                new UnityWebRequest(SERVER_URL + "/users/" + GetUserId(), "POST"))
            {
                if (!string.IsNullOrEmpty(json))
                {
                    byte[] bodyRaw = Encoding.UTF8.GetBytes(json);
                    webRequest.uploadHandler = new UploadHandlerRaw(bodyRaw);
                }

                webRequest.downloadHandler = new DownloadHandlerBuffer();
                webRequest.SetRequestHeader("Content-Type", "application/json");

                yield return webRequest.SendWebRequest();

                if (webRequest.isNetworkError || webRequest.isHttpError)
                {
                    onError(webRequest.error);
                }
                else
                {
                    onSuccess?.Invoke(
                        JsonMapper.ToObject<PlayerData>(webRequest.downloadHandler.text));
                }
            }
        }

        /// <summary>
        ///     Deletes the world data associated to our user id.
        /// </summary>
        /// <param name="onSuccess">The callback invoked when the call returns successfully</param>
        /// <param name="onError">The callback invoked if the call fails</param>
        /// <returns>An enumerator for the coroutine</returns>
        public IEnumerator DeleteWorldData(Action<WorldData> onSuccess, Action<string> onError)
        {
            using (UnityWebRequest webRequest =
                UnityWebRequest.Delete(SERVER_URL + "/worlds/" + GetUserId()))
            {
                // Request and wait for the desired page.
                yield return webRequest.SendWebRequest();

                if (webRequest.isNetworkError || webRequest.isHttpError)
                {
                    onError?.Invoke(webRequest.error);
                }
                else
                {
                    onSuccess?.Invoke(new WorldData());
                }
            }
        }

        /// <summary>
        ///     Returns the world data for the given Lat Lng rectangle.
        ///     This calls updates the server cache if new playable locations are found.
        /// </summary>
        /// <param name="worldDataRequest">World data request</param>
        /// <param name="onSuccess">The callback invoked when the call returns successfully</param>
        /// <param name="onError">The callback invoked if the call fails</param>
        /// <returns>An enumerator for the coroutine function</returns>
        public IEnumerator PostWorldData(
            WorldDataRequest worldDataRequest,
            Action<WorldData> onSuccess,
            Action<string> onError)
        {
            string json = "";
            if (worldDataRequest != null)
            {
                json = JsonMapper.ToJson(worldDataRequest);
            }

            using (UnityWebRequest webRequest =
                new UnityWebRequest(SERVER_URL + "/worlds/" + GetUserId(), "POST"))
            {
                byte[] bodyRaw = Encoding.UTF8.GetBytes(json);
                webRequest.uploadHandler = new UploadHandlerRaw(bodyRaw);
                webRequest.downloadHandler = new DownloadHandlerBuffer();
                webRequest.SetRequestHeader("Content-Type", "application/json");

                yield return webRequest.SendWebRequest();

                if (webRequest.isNetworkError || webRequest.isHttpError)
                {
                    onError?.Invoke(webRequest.error);
                }
                else
                {
                    onSuccess?.Invoke(
                        JsonMapper.ToObject<WorldData>(webRequest.downloadHandler.text));
                }
            }
        }

        /// <summary>
        ///     Returns a unique identifier for this player/device.
        ///     The user id is cached in <see cref="PlayerPrefs"/> when first created.
        ///     It is read from the cache if it already exists.
        /// </summary>
        /// <returns>A generated user id</returns>
        public string GetUserId()
        {
            // Retrieves a user Id from player prefs or generate a new one if can't be found.
            if (!PlayerPrefs.HasKey(GameConstants.PLAYER_ID))
            {
                PlayerPrefs.SetString(GameConstants.PLAYER_ID, Guid.NewGuid().ToString());
            }

            return PlayerPrefs.GetString(GameConstants.PLAYER_ID);
        }
    }
}
