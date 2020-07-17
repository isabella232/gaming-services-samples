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

using Google.Maps.Coord;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    /// This model describes a world data request.
    /// It provides information about the lat lng rectangle defined by the given north east
    /// and south west corners.
    /// </summary>
    public class WorldDataRequest
    {
        /// <summary>
        /// North east lat lng corner of a map region.
        /// </summary>
        public PlayableLocationLatLng northeast { get; set; }
        /// <summary>
        /// South west lat lng corner of a map region.
        /// </summary>
        public PlayableLocationLatLng southwest { get; set; }

        /// <summary>
        /// Helper function that initializes this PlayableLocation Lat Lng instance with
        /// the provided LatLng structures from the Maps Gaming SDK.
        /// </summary>
        /// <param name="southwestlatLng"></param>
        /// <param name="northeastlatLng"></param>
        public void CopyFrom(LatLng southwestlatLng, LatLng northeastlatLng)
        {
            northeast = new PlayableLocationLatLng();
            northeast.latitude = northeastlatLng.Lat;
            northeast.longitude = northeastlatLng.Lng;
            southwest = new PlayableLocationLatLng();
            southwest.latitude = southwestlatLng.Lat;
            southwest.longitude = southwestlatLng.Lng;
        }

        public override string ToString()
        {
            return "northeast: " + northeast + " southwest: " + southwest;
        }
    }
}
