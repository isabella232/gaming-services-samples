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
using System.Linq;
using System.Xml;

namespace Google.Maps.Demos.Zoinkies {

  /// <summary>
  /// This class provides access to world data through helper functions.
  /// </summary>
  public class WorldService {

    /// <summary>
    /// Indicates if player data has been set (at least once).
    /// </summary>
    public bool IsInitialized { get; set; }
    /// <summary>
    /// Indicates if world data has been modified in the client and needs to sync with the server.
    /// </summary>
    public bool DataHasChanged { get; set; }

    // Singleton
    private static WorldService instance;
    public static WorldService GetInstance() {
      if (instance == null) {
        instance = new WorldService();
      }
      return instance;
    }
    private WorldService() {
      IsInitialized = false;
    }

    internal WorldData data { get; set; }

    /// <summary>
    /// Initializes World Data.
    /// </summary>
    /// <param name="data"></param>
    /// <exception cref="Exception"></exception>
    public void Init(WorldData data) {
      if (data == null) {
        this.data = new WorldData();
      }
      else {
        this.data = data;
      }
      IsInitialized = true;
      DataHasChanged = false;

    }

    /// <summary>
    /// Returns spawn locations ids.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerable<String> GetSpawnLocationsIds() {
      if (data == null) {
        throw new System.Exception("World data not initialized!");
      }

      return data.locations.Keys;
    }

    public String GetLastServerTimeSnapshot() {
      if (data == null) {
        throw new System.Exception("World data not initialized!");
      }

      return data.currentServerTime;
    }

    /// <summary>
    /// Returns the spawn location identified by the given id.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public SpawnLocation GetSpawnLocation(string id) {
      if (data == null) {
        throw new System.Exception("World data not initialized!");
      }

      if (string.IsNullOrEmpty(id)) {
        throw new System.Exception("Invalid id found while trying to get a spawn location!");
      }

      if (!data.locations.ContainsKey(id)) {
        throw new System.Exception("Missing Spawn location with Id " + id + " from world data!");
      }

      return data.locations[id];
    }

    /// <summary>
    /// Indicates if the location identified by the given id is respawning.
    /// This function will update the location status (in the client) based on local time
    /// and respawn information.
    /// Note that this information will be overriden after the next server sync.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public bool IsRespawning(string Id) {
      if (string.IsNullOrEmpty(Id))
        throw new System.Exception("Invalid Id!");

      // Init the spawn location respawn time and active flag (to mimic what the server will do)
      // Prevents constant syncs - the server is the final authority
      SpawnLocation location = GetSpawnLocation(Id);
      if (location.respawn_time != null) {
        DateTime t = DateTime.Parse(location.respawn_time);
        if (t <= DateTime.Now) {
          // Has respawned
          location.respawn_time = null;
          location.active = true;
        }
        else {
          location.active = false;
        }
      }
      else {
        location.active = true;
      }
      return !location.active;
    }

    /// <summary>
    /// Starts respawn at the location identified by the given id, only if the location
    /// definition indicates that this it is respawnable.
    /// Note that this information will be overriden after the next server sync.
    /// </summary>
    /// <param name="Id"></param>
    /// <exception cref="Exception"></exception>
    public void StartRespawn(string Id) {
      if (string.IsNullOrEmpty(Id))
        throw new System.Exception("Invalid PlaceId!");

      // Init the spawn location respawn time and active flag (to mimic what the server will do)
      // Prevents constant syncs - the server is the final authority
      SpawnLocation location = WorldService.GetInstance().GetSpawnLocation(Id);
      ReferenceItem ri = ReferenceService.GetInstance().GetItem(location.object_type_id);
      if (location != null && ri != null && ri.respawnDuration != null) {
        location.active = false;
        TimeSpan ts = XmlConvert.ToTimeSpan(ri.respawnDuration);
        DateTime dt = DateTime.Now.Add(ts);
        location.respawn_time = dt.ToString("O");
        DataHasChanged = true;
      }
    }

    /// <summary>
    /// Returns all locations currently respawning.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerable<SpawnLocation> GetAllRespawningLocations() {
      if (data == null) {
        throw new System.Exception("World data not initialized!");
      }
      return data.locations.Values.Where(s => s.respawn_time != null);
    }

    /// <summary>
    /// Returns all towers locations.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerable<SpawnLocation> GetTowers() {
      if (data == null) {
        throw new System.Exception("World data not initialized!");
      }
      return data.locations.Values.Where(s => s.object_type_id == GameConstants.TOWER);
    }

    /// <summary>
    /// Returns all minion locations.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerable<SpawnLocation> GetMinions() {
      if (data == null) {
        throw new System.Exception("World data not initialized!");
      }
      return data.locations.Values.Where(s => s.object_type_id == GameConstants.MINION);
    }

    /// <summary>
    /// Returns all chests locations.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerable<SpawnLocation> GetChests() {
      if (data == null) {
        throw new System.Exception("World data not initialized!");
      }
      return data.locations.Values.Where(s => s.object_type_id == GameConstants.CHEST);
    }

    /// <summary>
    /// Returns all energy stations locations.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerable<SpawnLocation> GetEnergyStations() {
      if (data == null) {
        throw new System.Exception("World data not initialized!");
      }
      return data.locations.Values.Where(s => s.object_type_id == GameConstants.ENERGY_STATION);
    }

  }
}
