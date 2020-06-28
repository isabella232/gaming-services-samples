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

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    ///     This class provides access to world data through helper functions.
    /// </summary>
    public class WorldService
    {
        // Singleton pattern implementation
        private static WorldService _instance;
        public static WorldService GetInstance()
        {
            if (_instance == null)
            {
                _instance = new WorldService();
            }

            return _instance;
        }
        private WorldService()
        {
            IsInitialized = false;
        }

        /// <summary>
        ///     Indicates if player data has been set (at least once).
        /// </summary>
        public bool IsInitialized { get; set; }

        /// <summary>
        ///     Indicates if world data has been modified in the client and needs to sync with the server.
        /// </summary>
        public bool DataHasChanged { get; set; }

        /// <summary>
        /// Keeps a reference to the world data.
        /// World data can only be changed through the init function.
        /// </summary>
        private WorldData _data;

        /// <summary>
        ///     Initializes World Data.
        /// </summary>
        /// <param name="data">A new trusted world data</param>
        public void Init(WorldData data)
        {
            if (data == null)
            {
                this._data = new WorldData();
            }
            else
            {
                this._data = data;
            }

            IsInitialized = true;
            DataHasChanged = false;
        }

        /// <summary>
        ///     Returns spawn locations ids.
        /// </summary>
        /// <returns>A list of location ids</returns>
        /// <exception cref="Exception">Exception if world data is not initialized</exception>
        public IEnumerable<string> GetSpawnLocationsIds()
        {
            if (_data == null)
            {
                throw new System.Exception("World data not initialized!");
            }

            return _data.locations.Keys;
        }

        /// <summary>
        ///     Returns the spawn location identified by the given id.
        /// </summary>
        /// <param name="locationId">The location id</param>
        /// <returns>A Spawn location</returns>
        /// <exception cref="Exception">Exception if id is invalid</exception>
        public SpawnLocation GetSpawnLocation(string locationId)
        {
            if (_data == null)
            {
                throw new System.Exception("World data not initialized!");
            }

            if (string.IsNullOrEmpty(locationId))
            {
                throw new System.Exception(
                    "Invalid id found while trying to get a spawn location!");
            }

            if (!_data.locations.ContainsKey(locationId))
            {
                throw new System.Exception("Missing Spawn location with Id " + locationId +
                                           " from world data!");
            }

            return _data.locations[locationId];
        }

        /// <summary>
        ///     Indicates if the location identified by the given id is respawning.
        ///     This function will update the location status (in the client) based on local time
        ///     and respawn information.
        ///     Note that this information will be overriden after the next server sync.
        /// </summary>
        /// <param name="locationId">The location id</param>
        /// <returns>A boolean to indicate if the location is respawning.</returns>
        /// <exception cref="Exception">Exception if id is invalid</exception>
        public bool IsRespawning(string locationId)
        {
            if (string.IsNullOrEmpty(locationId))
            {
                throw new System.Exception("Invalid Id!");
            }

            // Init the spawn location respawn time and active flag (to mimic what the server will do)
            // Prevents constant syncs - the server is the final authority
            SpawnLocation location = GetSpawnLocation(locationId);
            if (location.respawn_time != null)
            {
                DateTime t = DateTime.Parse(location.respawn_time);
                if (t <= DateTime.Now)
                {
                    location.respawn_time = null;
                    location.active = true;
                }
                else
                {
                    location.active = false;
                }
            }
            else
            {
                location.active = true;
            }

            return !location.active;
        }

        /// <summary>
        ///     Starts respawn at the location identified by the given id, only if the location
        ///     definition indicates that this it is respawnable.
        ///     Note that this information will be overriden after the next server sync.
        /// </summary>
        /// <param name="locationId">The location id</param>
        /// <exception cref="Exception">Exception if id is invalid</exception>
        public void StartRespawn(string locationId)
        {
            if (string.IsNullOrEmpty(locationId))
            {
                throw new System.Exception("Invalid Location Id!");
            }

            // Init the spawn location respawn time and active flag (to mimic what the server will do)
            // Prevents constant syncs - the server is the final authority
            SpawnLocation location = GetInstance().GetSpawnLocation(locationId);
            ReferenceItem ri = ReferenceService.GetInstance().GetItem(location.object_type_id);
            if (location != null && ri != null && ri.respawnDuration != null)
            {
                location.active = false;
                TimeSpan ts = XmlConvert.ToTimeSpan(ri.respawnDuration);
                DateTime dt = DateTime.Now.Add(ts);
                location.respawn_time = dt.ToString("O");
                DataHasChanged = true;
            }
        }

        /// <summary>
        ///     Returns all locations currently respawning.
        /// </summary>
        /// <returns>A list of Spawn Locations</returns>
        /// <exception cref="Exception">Exception if world data is not initialized</exception>
        public IEnumerable<SpawnLocation> GetAllRespawningLocations()
        {
            if (_data == null)
            {
                throw new System.Exception("World data not initialized!");
            }

            return _data.locations.Values.Where(s => s.respawn_time != null);
        }

        /// <summary>
        ///     Returns all towers locations.
        /// </summary>
        /// <returns>A list of Spawn Locations</returns>
        /// <exception cref="Exception">Exception if world data is not initialized</exception>
        public IEnumerable<SpawnLocation> GetTowers()
        {
            if (_data == null)
            {
                throw new System.Exception("World data not initialized!");
            }

            return _data.locations.Values.Where(s => s.object_type_id == GameConstants.TOWER);
        }

        /// <summary>
        ///     Returns all minion locations.
        /// </summary>
        /// <returns>A list of Spawn Locations</returns>
        /// <exception cref="Exception">Exception if world data is not initialized</exception>
        public IEnumerable<SpawnLocation> GetMinions()
        {
            if (_data == null)
            {
                throw new System.Exception("World data not initialized!");
            }

            return _data.locations.Values.Where(s => s.object_type_id == GameConstants.MINION);
        }

        /// <summary>
        ///     Returns all chests locations.
        /// </summary>
        /// <returns>A list of Spawn Locations</returns>
        /// <exception cref="Exception">Exception if world data is not initialized</exception>
        public IEnumerable<SpawnLocation> GetChests()
        {
            if (_data == null)
            {
                throw new System.Exception("World data not initialized!");
            }

            return _data.locations.Values.Where(s => s.object_type_id == GameConstants.CHEST);
        }

        /// <summary>
        ///     Returns all energy stations locations.
        /// </summary>
        /// <returns>A list of Spawn Locations</returns>
        /// <exception cref="Exception">Exception if world data is not initialized</exception>
        public IEnumerable<SpawnLocation> GetEnergyStations()
        {
            if (_data == null)
            {
                throw new System.Exception("World data not initialized!");
            }

            return _data.locations.Values.Where(
                s => s.object_type_id == GameConstants.ENERGY_STATION);
        }
    }
}
