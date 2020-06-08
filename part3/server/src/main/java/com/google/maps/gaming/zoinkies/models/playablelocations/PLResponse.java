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

import java.util.Map;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class PLResponse {
  private Map<String, PLLocations> locationsPerGameObjectType;
  private String ttl;

  public String getTtl() {
    return ttl;
  }

  public void setTtl(String ttl) {
    this.ttl = ttl;
  }

  public Map<String, PLLocations> getLocationsPerGameObjectType() {
    return locationsPerGameObjectType;
  }

  public void setLocationsPerGameObjectType(
      Map<String, PLLocations> locationsPerGameObjectType) {
    this.locationsPerGameObjectType = locationsPerGameObjectType;
  }
}
