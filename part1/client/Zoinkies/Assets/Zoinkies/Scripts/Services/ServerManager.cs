using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using LitJson;
using UnityEngine;
using UnityEngine.Networking;

namespace Google.Maps.Demos.Zoinkies {

  /// <summary>
  /// Server manager that makes all REST API calls to the game server.
  /// </summary>
  public class ServerManager : MonoBehaviour {
    //private const string SERVER_URL = "http://localhost:8080";
    //private const string SERVER_URL = "https://musk-samples.wl.r.appspot.com";


    /*
    /// <summary>
    /// Sends battle results to the server.
    /// The server will compute the rewards (or the penalty) depending on who wins.
    ///
    /// </summary>
    /// <param name="placeId"></param>
    /// <param name="Winner"></param>
    /// <param name="onSuccess"></param>
    /// <param name="onError"></param>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerator PostBattleSummary(String placeId,
      bool Winner,
      Action<BattleSummaryData> onSuccess,
      Action<string> onError) {

      if (string.IsNullOrEmpty(placeId))
        throw new System.Exception("Invalid Place Id!");

      using (UnityWebRequest webRequest =
        new UnityWebRequest(
          SERVER_URL + "/battlesummary/" + GetUserId() + "/" + placeId + "?winner=" + Winner,
          "POST")) {

        webRequest.downloadHandler = new DownloadHandlerBuffer();
        webRequest.SetRequestHeader("Content-Type", "application/json");

        yield return webRequest.SendWebRequest();

        if (webRequest.isNetworkError || webRequest.isHttpError) {
          Debug.Log(webRequest.error);
          onError(webRequest.error);
        }
        else {

          if (webRequest.responseCode != 204) {
            Debug.Log(webRequest.downloadHandler.text);
            onSuccess(JsonMapper.ToObject<BattleSummaryData>(webRequest.downloadHandler.text));
          }
          else {
            // Station recharging
            onSuccess(null);
          }
        }
      }
    }

    /// <summary>
    /// Creates or updates a new battle on the server. The data returned describes the parameter for
    /// the encounter.
    /// </summary>
    /// <param name="placeId"></param>
    /// <param name="onSuccess"></param>
    /// <param name="onError"></param>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerator PostBattleData(String placeId,
      Action<BattleData> onSuccess,
      Action<string> onError) {

      if (string.IsNullOrEmpty(placeId))
        throw new System.Exception("Invalid Place Id!");

      using (UnityWebRequest webRequest =
        new UnityWebRequest(SERVER_URL + "/battle/" + GetUserId() + "/" + placeId, "POST")) {

        webRequest.downloadHandler = new DownloadHandlerBuffer();
        webRequest.SetRequestHeader("Content-Type", "application/json");

        yield return webRequest.SendWebRequest();

        if (webRequest.isNetworkError || webRequest.isHttpError) {
          Debug.Log(webRequest.error);
          onError(webRequest.error);
        }
        else {
          if (webRequest.responseCode != 204) {
            onSuccess(JsonMapper.ToObject<BattleData>(webRequest.downloadHandler.text));
          }
          else {
            // Station recharging
            onSuccess(null);
          }
        }
      }
    }

    /// <summary>
    /// Requests an energy recharge for our avatar to the server.
    /// The data returned indicates how much energy was restored.
    ///
    /// </summary>
    /// <param name="placeId"></param>
    /// <param name="onSuccess"></param>
    /// <param name="onError"></param>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerator PostRechargingStation(String placeId,
      Action<EnergyData> onSuccess,
      Action<string> onError) {

      if (string.IsNullOrEmpty(placeId))
        throw new System.Exception("Invalid Place Id!");

      String json = "";

      using (UnityWebRequest webRequest =
        new UnityWebRequest(SERVER_URL + "/energystation/" + GetUserId() + "/" + placeId, "POST")) {
        if (!string.IsNullOrEmpty(json)) {
          byte[] bodyRaw = Encoding.UTF8.GetBytes(json);
          webRequest.uploadHandler = new UploadHandlerRaw(bodyRaw);
        }

        webRequest.downloadHandler = new DownloadHandlerBuffer();
        webRequest.SetRequestHeader("Content-Type", "application/json");

        yield return webRequest.SendWebRequest();

        if (webRequest.isNetworkError || webRequest.isHttpError) {
          Debug.Log(webRequest.error);
          onError(webRequest.error);
        }
        else {

          if (webRequest.responseCode != 204) {
            Debug.Log(webRequest.downloadHandler.text);
            EnergyData data = JsonMapper.ToObject<EnergyData>(webRequest.downloadHandler.text);
            onSuccess(data);
          }
          else {
            // Station recharging
            onSuccess(null);
          }
        }
      }
    }

    /// <summary>
    /// Notifies the server that the player is opening a chest.
    /// Returns the content of the chest if this action is validated by the server.
    /// </summary>
    /// <param name="placeId"></param>
    /// <param name="onSuccess"></param>
    /// <param name="onError"></param>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerator PostChest(String placeId,
      Action<RewardsData> onSuccess,
      Action<string> onError) {

      if (string.IsNullOrEmpty(placeId))
        throw new System.Exception("Invalid Place Id!");

      String json = "";

      using (UnityWebRequest webRequest =
        new UnityWebRequest(SERVER_URL + "/chests/" + GetUserId() + "/" + placeId, "POST")) {
        if (!string.IsNullOrEmpty(json)) {
          byte[] bodyRaw = Encoding.UTF8.GetBytes(json);
          webRequest.uploadHandler = new UploadHandlerRaw(bodyRaw);
        }

        webRequest.downloadHandler = new DownloadHandlerBuffer();
        webRequest.SetRequestHeader("Content-Type", "application/json");

        yield return webRequest.SendWebRequest();

        if (webRequest.isNetworkError || webRequest.isHttpError) {
          Debug.Log(webRequest.error);
          onError(webRequest.error);
        }
        else {

          if (webRequest.responseCode != 204) {
            Debug.Log(webRequest.downloadHandler.text);
            onSuccess(JsonMapper.ToObject<RewardsData>(webRequest.downloadHandler.text));
          }
          else {
            // Chest respawning
            onSuccess(null);
          }
        }
      }
    }
    */

    public ReferenceData GetReferenceData() {
      TextAsset targetFile = Resources.Load<TextAsset>("ReferenceData");
      if (targetFile != null) {
        return JsonMapper.ToObject<ReferenceData>(targetFile.text);
      }

      throw new System.Exception("Can't load reference data!");
    }

    public PlayerData GetPlayerData() {

      PlayerData data = new PlayerData();

      List<Item> inventory = new List<Item>();
      inventory.Add(new Item(GameConstants.BODY_ARMOR_TYPE_1, 1));
      inventory.Add(new Item(GameConstants.WEAPON_TYPE_1, 1));
      // Character types are added to the default inventory
      inventory.Add(new Item(GameConstants.CHARACTER_TYPE_1, 1));
      inventory.Add(new Item(GameConstants.CHARACTER_TYPE_2, 1));
      inventory.Add(new Item(GameConstants.CHARACTER_TYPE_3, 1));
      inventory.Add(new Item(GameConstants.CHARACTER_TYPE_4, 1));

      data.name = GameConstants.DEFAULT_PLAYER_NAME;
      data.characterType = GameConstants.CHARACTER_TYPE_1;
      data.energyLevel = GameConstants.DEFAULT_PLAYER_ENERGY_LEVEL;
      data.maxEnergyLevel = GameConstants.DEFAULT_PLAYER_ENERGY_LEVEL;
      data.inventory = inventory;
      // Equip body armor and weapon starters
      data.equippedBodyArmor = GameConstants.BODY_ARMOR_TYPE_1;
      data.equippedWeapon = GameConstants.WEAPON_TYPE_1;

      return data;
    }

    /*
    /// <summary>
    /// Loads reference data from the game server.
    /// This data is now accessible with the ReferenceService.
    /// </summary>
    /// <returns></returns>
    public IEnumerator GetReferenceData(Action<ReferenceData> onSuccess, Action<string> onError) {
      Debug.Log("Requesting reference data");

      // Read from file for now



      using (UnityWebRequest webRequest = UnityWebRequest.Get(SERVER_URL + "/references")) {
        // Request and wait for the desired page.
        yield return webRequest.SendWebRequest();

        if (webRequest.isNetworkError || webRequest.isHttpError) {
          Debug.Log(webRequest.error);
          onError(webRequest.error);
        }
        else {
          // Show results as text
          onSuccess?.Invoke(JsonMapper.ToObject<ReferenceData>(webRequest.downloadHandler.text));
        }
      }


    }
    */

    /*
    /// <summary>
    /// Loads stats and inventory for the given player.
    /// </summary>
    /// <returns></returns>
    public IEnumerator GetPlayerData(Action<PlayerData> onSuccess, Action<string> onError) {
      Debug.Log("Requesting player data");
      using (UnityWebRequest webRequest = UnityWebRequest.Get(SERVER_URL + "/users/" + GetUserId())
      ) {
        Debug.Log(SERVER_URL + "/users/" + GetUserId());

        // Request and wait for the desired page.
        yield return webRequest.SendWebRequest();

        if (webRequest.isNetworkError || webRequest.isHttpError) {
          Debug.Log(webRequest.error);
          onError(webRequest.error);
        }
        else {
          // No record
          if (webRequest.responseCode != 204) {
            // Show results as text
            string json = webRequest.downloadHandler.text;
            onSuccess?.Invoke(JsonMapper.ToObject<PlayerData>(json));
          }
          else {
            Debug.Log("No data found for " + GetUserId());
            // Create it
            StartCoroutine(PostPlayerData(null, onSuccess, onError));
          }
        }
      }
    }

    /// <summary>
    /// Creates (stats and inventory) or updates (stats) of the current player.
    /// Note that the inventory is managed by the game server.
    ///
    /// </summary>
    /// <param name="data"></param>
    /// <returns></returns>
    public IEnumerator PostPlayerData(PlayerData data,
      Action<PlayerData> onSuccess,
      Action<string> onError) {
      Debug.Log("PostPlayerData");

      string json = "";
      if (data != null) {
        json = JsonMapper.ToJson(data);
      }

      using (UnityWebRequest webRequest =
        new UnityWebRequest(SERVER_URL + "/users/" + GetUserId(), "POST")) {
        if (!string.IsNullOrEmpty(json)) {
          byte[] bodyRaw = Encoding.UTF8.GetBytes(json);
          webRequest.uploadHandler = new UploadHandlerRaw(bodyRaw);
        }

        webRequest.downloadHandler = new DownloadHandlerBuffer();
        webRequest.SetRequestHeader("Content-Type", "application/json");

        yield return webRequest.SendWebRequest();

        if (webRequest.isNetworkError || webRequest.isHttpError) {
          Debug.Log(webRequest.error);
          onError(webRequest.error);
        }
        else {
          Debug.Log(webRequest.downloadHandler.text);
          onSuccess?.Invoke(JsonMapper.ToObject<PlayerData>(webRequest.downloadHandler.text));
        }
      }
    }

    /*
    /// <summary>
    /// Deletes the world data associated to our user id.
    /// </summary>
    /// <param name="onSuccess"></param>
    /// <param name="onError"></param>
    /// <returns></returns>
    public IEnumerator DeleteWorldData(Action<WorldData> onSuccess, Action<string> onError) {
      using (UnityWebRequest webRequest =
        UnityWebRequest.Delete(SERVER_URL + "/worlds/" + GetUserId())) {
        Debug.Log(SERVER_URL + "/users/" + GetUserId());

        // Request and wait for the desired page.
        yield return webRequest.SendWebRequest();

        if (webRequest.isNetworkError || webRequest.isHttpError) {
          Debug.Log(webRequest.error);
          onError?.Invoke(webRequest.error);
        }
        else {
          //WorldService.data = new WorldData();
          onSuccess?.Invoke(new WorldData());
        }
      }
    }

    /// <summary>
    /// Returns the world data for the given Lat Lng rectangle.
    /// This calls updates the server cache if new playable locations are found.
    /// </summary>
    /// <returns></returns>
    public IEnumerator PostWorldData(WorldDataRequest wdr,
      Action<WorldData> onSuccess,
      Action<string> onError) {
      Debug.Log("Requesting world data");
      string json = "";
      if (wdr != null) {
        json = JsonMapper.ToJson(wdr);
      }

      using (UnityWebRequest webRequest =
        new UnityWebRequest(SERVER_URL + "/worlds/" + GetUserId(), "POST")) {
        byte[] bodyRaw = Encoding.UTF8.GetBytes(json);
        webRequest.uploadHandler = new UploadHandlerRaw(bodyRaw);
        webRequest.downloadHandler = new DownloadHandlerBuffer();
        webRequest.SetRequestHeader("Content-Type", "application/json");

        yield return webRequest.SendWebRequest();

        if (webRequest.isNetworkError || webRequest.isHttpError) {
          onError?.Invoke(webRequest.error);
        }
        else {
          onSuccess?.Invoke(JsonMapper.ToObject<WorldData>(webRequest.downloadHandler.text));
        }
      }
    }
    */

    /// <summary>
    /// Returns a unique identifier for this player/device
    /// </summary>
    /// <returns></returns>
    public String GetUserId() {
      // Retrieves a user Id from player prefs or generate a new one if can't be found.
      if (!PlayerPrefs.HasKey(GameConstants.PLAYER_ID)) {
        PlayerPrefs.SetString(GameConstants.PLAYER_ID, Guid.NewGuid().ToString());
      }

      return PlayerPrefs.GetString(GameConstants.PLAYER_ID);
    }
  }
}
