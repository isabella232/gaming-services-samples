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
package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * Enumeration of point types to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public enum PLPointType {
  /// <summary>Unspecified point type. Do not use this value.</summary>
  Unspecified,

  /// <summary>
  /// The geographic coordinates correspond to the center of the location.
  /// </summary>
  CenterPoint,

  /// <summary>
  /// The geographic coordinates correspond to the location snapped to the
  /// sidewalk of the nearest road (when a nearby road exists).
  /// </summary>
  SnappedPoint,
}
