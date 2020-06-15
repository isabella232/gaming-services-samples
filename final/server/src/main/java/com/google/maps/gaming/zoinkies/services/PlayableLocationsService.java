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
package com.google.maps.gaming.zoinkies.services;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2CellUnion;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import com.google.common.geometry.S2RegionCoverer;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLAreaFilter;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLCriteria;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLFieldMask;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLFilter;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLLatLng;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLLocation;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLLocations;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLRequest;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * The playable location service provides logic to persist and manipulate world data.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PlayableLocationsService {

  private final String PLAYABLE_LOCATION_URL =
      "https://playablelocations.googleapis.com/v3:samplePlayableLocations";
  private final int S2_CELL_LEVEL = 11;
  private final int S2_CELL_MAX_LEVEL = 14;
  private final String API_KEY = "AIzaSyAZc7i2gq-bv-xYy2Ou2eNDPDUxf5bIQEo"; // TODO Remove before publishing
  private final int GAME_OBJECT_TYPE_SPAWN_LOCATIONS = 0;

  /**
   * Loads all playable locations within the S2 cells overlapping with the rectangle area
   * identified by the given north east and south west corners.
   * The search area is potentially way larger from the queried area.
   *
   * @return
   */
  public PLResponse RequestPlayableLocations(PLLatLng loLatLng, PLLatLng hiLatLng,
      PLCriteria[] criteria) throws Exception {

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("x-goog-api-key", API_KEY);

    // Build query
    PLRequest req = new PLRequest();
    req.setAreaFilter(new PLAreaFilter());
    if (criteria == null) {
      req.setCriteria(GetPLDefaultCriteria());
    } else {
      req.setCriteria(criteria);
    }

    // Configure a region coverer, which will help us get all overlapping S2 cells on the
    // given Lat Lng rectangle
    S2RegionCoverer rc = new S2RegionCoverer();
    rc.setMinLevel(this.S2_CELL_LEVEL);
    rc.setMaxLevel(this.S2_CELL_MAX_LEVEL);

    // Get the two opposite corners in degrees.
    S2LatLng lo = S2LatLng.fromDegrees(loLatLng.getLatitude(),loLatLng.getLongitude());
    S2LatLng hi = S2LatLng.fromDegrees(hiLatLng.getLatitude(),hiLatLng.getLongitude());

    // Define the Lat Lng Rectangle
    S2LatLngRect r = new S2LatLngRect(lo,hi);

    // Get all cells that are covering the provided area
    S2CellUnion cu = rc.getCovering(r);

    PLResponse combinedResponse = new PLResponse();
    String objectType = Integer.toString(GAME_OBJECT_TYPE_SPAWN_LOCATIONS);
    combinedResponse.setLocationsPerGameObjectType(new HashMap<String, PLLocations>());
    combinedResponse.getLocationsPerGameObjectType().put(objectType,new PLLocations());

    List<PLLocation> combinedLocations = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    // For each overlapping cell, query playable locations API and merge results into
    // a combined response.
    for (S2CellId id:cu.cellIds()){
      req.getAreaFilter().setS2CellId(Long.toUnsignedString(id.id()));

      String reqJson = objectMapper.writeValueAsString(req);
      System.out.println(reqJson);

      HttpEntity<String> request = new HttpEntity<String>(reqJson, headers);
      String plResponse = restTemplate.postForObject(PLAYABLE_LOCATION_URL, request, String.class);
      assertNotNull(plResponse);

      PLResponse response = objectMapper.readValue(plResponse,PLResponse.class);
      combinedResponse.setTtl(response.getTtl());
      combinedLocations.addAll(Arrays.asList(
          response.getLocationsPerGameObjectType().get(objectType).getLocations()));
    }

    combinedResponse.getLocationsPerGameObjectType().get(objectType).setLocations(
        combinedLocations.toArray(new PLLocation[0]));
    return combinedResponse;
  }

  /**
   * Provides a default criteria for the playable locations request.
   *
   * @return
   */
  private PLCriteria[] GetPLDefaultCriteria() {
    PLCriteria[] plc = new PLCriteria[1];
    plc[0] = new PLCriteria();
    plc[0].setGame_object_type(GAME_OBJECT_TYPE_SPAWN_LOCATIONS);
    plc[0].setFilter( new PLFilter());
    plc[0].getFilter().setMax_location_count(2);
    plc[0].setFields_to_return( new PLFieldMask());
    plc[0].getFields_to_return().setPaths( new String[]{"snapped_point", "place_id", "types"});
    return plc;
  }
}