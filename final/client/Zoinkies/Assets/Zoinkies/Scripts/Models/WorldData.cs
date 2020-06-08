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
using System.Text;

namespace Google.Maps.Demos.Zoinkies {

    /// <summary>
    /// Keeps track of all game locations.
    /// </summary>
    public class WorldData {
        /// <summary>
        /// List of spawn locations
        /// </summary>
        public Dictionary<string, SpawnLocation> locations { get; set; }

        /// <summary>
        /// Current server time
        /// </summary>
        public string currentServerTime { get; set; }

        public WorldData() {
            locations = new Dictionary<string, SpawnLocation>();
            currentServerTime = DateTime.UtcNow.ToString();
        }

        public override string ToString() {
            StringBuilder sb = new StringBuilder();
            sb.Append("Locations: \n");
            foreach (SpawnLocation loc in locations.Values) {
                sb.Append(loc.ToString() + " \t");
            }

            sb.Append("----------------------");

            return sb.ToString();
        }
    }
}
