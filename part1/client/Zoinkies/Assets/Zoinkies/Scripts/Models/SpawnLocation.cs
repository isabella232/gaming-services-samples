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

using System.Text;

namespace Google.Maps.Demos.Zoinkies
{
    public class SpawnLocation
    {
        /// <summary>
        ///     Unique identifier for the location
        /// </summary>
        public string locationId { get; set; }

        /// <summary>
        ///     Indicates if the location is active
        /// </summary>
        public bool active { get; set; }

        /// <summary>
        ///     The type of object at this location
        /// </summary>
        public string objectTypeId { get; set; }

        /// <summary>
        ///     Indicates if the location respawns
        /// </summary>
        public bool respawns { get; set; }

        /// <summary>
        ///     Indicates when the location respawns (ISO 8601)
        /// </summary>
        public string respawnTime { get; set; }

        /// <summary>
        ///     Number of "keys" to activate this location (if any)
        /// </summary>
        public int numberOfKeysToActivate { get; set; }

        /// <summary>
        ///     The key type for activating this location - could be anything.
        /// </summary>
        public string keyTypeId { get; set; }

        /// <summary>
        ///     Actual lat lng for this location.
        /// </summary>
        public PlayableLocationLatLng snappedPoint { get; set; }

        public SpawnLocation()
        {
        }

        public SpawnLocation(
            string locationId,
            bool active,
            string objectTypeId,
            bool respawns,
            string respawnTime,
            int numberOfKeysToActivate,
            string keyTypeId,
            PlayableLocationLatLng snappedPoint)
        {
            this.locationId = locationId;
            this.active = active;
            this.objectTypeId = objectTypeId;
            this.respawns = respawns;
            this.keyTypeId = keyTypeId;
            this.respawnTime = respawnTime;
            this.numberOfKeysToActivate = numberOfKeysToActivate;
            this.snappedPoint = snappedPoint;
        }

        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("Id: " + locationId + "  object_type_id " + objectTypeId);

            return sb.ToString();
        }
    }
}
