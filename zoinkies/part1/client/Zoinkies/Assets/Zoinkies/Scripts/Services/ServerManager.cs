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
using LitJson;
using UnityEngine;

namespace Google.Maps.Demos.Zoinkies {

  /// <summary>
  /// Server manager placeholder. Right now it only returns static data.
  /// </summary>
  public class ServerManager : MonoBehaviour {

    /// <summary>
    /// Returns the reference data for this game.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public ReferenceData GetReferenceData() {
      TextAsset targetFile = Resources.Load<TextAsset>("ReferenceData");
      if (targetFile != null) {
        return JsonMapper.ToObject<ReferenceData>(targetFile.text);
      }

      throw new System.Exception("Can't load reference data!");
    }

    /// <summary>
    /// Returns the player data.
    /// </summary>
    /// <returns>Player Data</returns>
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

    /// <summary>
    /// Returns a unique identifier for this player/device
    /// </summary>
    /// <returns>The generated id</returns>
    public String GetUserId() {
      // Retrieves a user Id from player prefs or generate a new one if can't be found.
      if (!PlayerPrefs.HasKey(GameConstants.PLAYER_ID)) {
        PlayerPrefs.SetString(GameConstants.PLAYER_ID, Guid.NewGuid().ToString());
      }

      return PlayerPrefs.GetString(GameConstants.PLAYER_ID);
    }
  }
}