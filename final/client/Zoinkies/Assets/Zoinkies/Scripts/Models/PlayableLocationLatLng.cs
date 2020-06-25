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

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    ///     Simple class to keep track of a latitude longitude pair.
    ///     This class, which holds similar information than the LatLng class
    ///     from the Maps Gaming SDK, is used to serialize/deserialize the attributes
    ///     of the json data provided by the server.
    /// </summary>
    public class PlayableLocationLatLng
    {
        /// <summary>
        /// Latitude of spawned location
        /// </summary>
        public double latitude { get; set; }
        /// <summary>
        /// Longitude of spawned location
        /// </summary>
        public double longitude { get; set; }

        public override string ToString()
        {
            return "latitude: " + latitude + "longitude: " + longitude;
        }
    }
}
