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
using System.Text;

namespace Google.Maps.Demos.Zoinkies {

    public class SpawnLocation {
        /// <summary>
        /// Unique identifier for the location
        /// </summary>
        public string id { get; set; }
        /// <summary>
        /// Indicates if the location is active
        /// </summary>
        public bool active { get; set; }
        /// <summary>
        /// The type of object at this location
        /// </summary>
        public string object_type_id { get; set; }
        /// <summary>
        /// Indicates if the location respawns
        /// </summary>
        public bool respawns { get; set; }
        /// <summary>
        /// Indicates when the location respawns (ISO 8601)
        /// </summary>
        public string respawn_time { get; set; }
        /// <summary>
        /// Number of "keys" to activate this location (if any)
        /// </summary>
        public int number_of_keys_to_activate { get; set; }
        /// <summary>
        /// The key type for activating this location - could be anything.
        /// </summary>
        public string key_type_id { get; set; }
        /// <summary>
        /// Actual lat lng for this location.
        /// </summary>
        public PLLatLng snappedPoint { get; set; }

        public SpawnLocation() {}

        public SpawnLocation(string id,
            Boolean active,
            string object_type_id,
            bool respawns,
            string respawn_time,
            int number_of_keys_to_activate,
            string key_type_id,
            PLLatLng snappedPoint) {

            this.id = id;
            this.active = active;
            this.object_type_id = object_type_id;
            this.respawns = respawns;
            this.key_type_id = key_type_id;
            this.respawn_time = respawn_time;
            this.number_of_keys_to_activate = number_of_keys_to_activate;
            this.snappedPoint = snappedPoint;
        }

        public override string ToString() {
            StringBuilder sb = new StringBuilder();
            sb.Append("Id: " + id + "  object_type_id " + object_type_id );

            return sb.ToString();
        }
   }
}
