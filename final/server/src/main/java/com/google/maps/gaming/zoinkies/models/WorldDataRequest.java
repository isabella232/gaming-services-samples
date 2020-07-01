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
package com.google.maps.gaming.zoinkies.models;

import com.google.maps.gaming.zoinkies.models.playablelocations.LatLng;

/**
 * A POJO class used to keep track of the client's world request.
 * The client provides a Lat Lng Rectangle, which is used to find all playable locations
 * within the overlapping S2 cells.
 */
public class WorldDataRequest {
  private LatLng northeast;
  private LatLng southwest;

  public LatLng getNortheast() {
    return northeast;
  }

  public void setNortheast(LatLng northeast) {
    this.northeast = northeast;
  }

  public LatLng getSouthwest() {
    return southwest;
  }

  public void setSouthwest(LatLng southwest) {
    this.southwest = southwest;
  }
}
