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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2CellUnion;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import com.google.common.geometry.S2RegionCoverer;
import com.google.maps.gaming.zoinkies.models.playablelocations.AreaFilter;
import com.google.maps.gaming.zoinkies.models.playablelocations.Criteria;
import com.google.maps.gaming.zoinkies.models.playablelocations.FieldMask;
import com.google.maps.gaming.zoinkies.models.playablelocations.Filter;
import com.google.maps.gaming.zoinkies.models.playablelocations.LatLng;
import com.google.maps.gaming.zoinkies.models.playablelocations.Location;
import com.google.maps.gaming.zoinkies.models.playablelocations.Locations;
import com.google.maps.gaming.zoinkies.models.playablelocations.Request;
import com.google.maps.gaming.zoinkies.models.playablelocations.Response;
import java.time.Duration;
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
 * The playable location service provides logic to persist and manipulate world
 * data.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PlayableLocationsService {

  /**
   * API key providing access to PlayableLocation API
   */
  private final String API_KEY = "<YOUR API KEY HERE>";
  /**
   * The url to the playable location API
   */
  private final String PLAYABLE_LOCATION_URL = "https://playablelocations.googleapis.com/v3:samplePlayableLocations";
  /**
   * Min S2Cell level required when processing cells covering the lat lng
   * rectangle
   */
  private final int S2_CELL_LEVEL = 11;
  /**
   * Max S2Cell level required when processing cells covering the lat lng
   * rectangle
   */
  private final int S2_CELL_MAX_LEVEL = 14;
  /**
   * Default parameters for object type when querying playable locations API
   */
  private final int GAME_OBJECT_TYPE_SPAWN_LOCATIONS = 0;

  /**
   * Loads all playable locations within the S2 cells overlapping with the
   * rectangle area identified by the given north east and south west corners. The
   * search area is potentially way larger from the queried area.
   *
   * @return A Playable Location Response
   */
  public Response requestPlayableLocations(LatLng loLatLng, LatLng hiLatLng, Criteria[] criteria,
      HashMap<String, String> PlayableLocationsCache) throws Exception {

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("x-goog-api-key", API_KEY);

    Request request = new Request();
    request.setAreaFilter(new AreaFilter());
    if (criteria == null) {
      request.setCriteria(getDefaultCriteria());
    } else {
      request.setCriteria(criteria);
    }

    // Configure a region coverer, which will help us get all overlapping S2 cells
    // on the
    // given Lat Lng rectangle
    S2RegionCoverer regionCoverer = new S2RegionCoverer();
    regionCoverer.setMinLevel(this.S2_CELL_LEVEL);
    regionCoverer.setMaxLevel(this.S2_CELL_MAX_LEVEL);

    // Get the two opposite corners in degrees.
    S2LatLng lo = S2LatLng.fromDegrees(loLatLng.getLatitude(), loLatLng.getLongitude());
    S2LatLng hi = S2LatLng.fromDegrees(hiLatLng.getLatitude(), hiLatLng.getLongitude());

    // Define the Lat Lng Rectangle
    S2LatLngRect latLngRect = new S2LatLngRect(lo, hi);

    // Get all cells that are covering the provided area
    S2CellUnion cellUnion = regionCoverer.getCovering(latLngRect);

    Response combinedResponse = new Response();
    String objectType = Integer.toString(GAME_OBJECT_TYPE_SPAWN_LOCATIONS);
    combinedResponse.setLocationsPerGameObjectType(new HashMap<String, Locations>());
    combinedResponse.getLocationsPerGameObjectType().put(objectType, new Locations());

    List<Location> combinedLocations = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    // For each overlapping cell, query playable locations API and merge results
    // into
    // a combined response.
    // If we've already queried a cell, check its TTL.
    // If the TTL is still valid, skip that cell as we've already handled the
    // playable locations
    // within.
    for (S2CellId id : cellUnion.cellIds()) {
      String cellIdString = Long.toUnsignedString(id.id());
      if (PlayableLocationsCache != null && PlayableLocationsCache.containsKey(cellIdString)) {
        Duration duration = Duration.parse(PlayableLocationsCache.get(cellIdString));
        if (duration.getSeconds() > 0) {
          continue;
        }
      }

      // The code below handles cells that haven't been processed yet as they are
      // missing
      // from our cache.
      // All playable locations returned within that cell are tagged with a cell id.
      request.getAreaFilter().setS2CellId(cellIdString);

      String reqJson = objectMapper.writeValueAsString(request);
      HttpEntity<String> httpEntity = new HttpEntity<String>(reqJson, headers);

      String playableLocationsResponse = restTemplate.postForObject(PLAYABLE_LOCATION_URL, httpEntity, String.class);
      if (playableLocationsResponse == null) {
        throw new Exception("Received an invalid playableLocationsResponse! (null)");
      }

      Response response = objectMapper.readValue(playableLocationsResponse, Response.class);

      if (response == null) {
        throw new Exception("Error while deserializing playable locations response.");
      }

      if (response.getLocationsPerGameObjectType() == null) {
        throw new Exception("Error: could not find a valid locations per gameobject type.");
      }

      if (response.getLocationsPerGameObjectType().get(objectType) == null) {
        throw new Exception("Error: no valid locations data for playable locations object type:" + objectType);
      }

      if (response.getLocationsPerGameObjectType().get(objectType).getLocations() == null) {
        throw new Exception("Error: found no locations for current request.");
      }

      combinedResponse.setTtl(response.getTtl());

      for (Location location : response.getLocationsPerGameObjectType().get(objectType).getLocations()) {
        if (location != null) {
          location.setS2CellId(request.getAreaFilter().getS2CellId());
        }
      }

      combinedLocations.addAll(Arrays.asList(response.getLocationsPerGameObjectType().get(objectType).getLocations()));

      // Update the cache
      if (PlayableLocationsCache != null && !PlayableLocationsCache.containsKey(request.getAreaFilter().getS2CellId())
          && response.getTtl() != null && !response.getTtl().isEmpty()) {
        PlayableLocationsCache.put(request.getAreaFilter().getS2CellId(), "PT" + response.getTtl());
      }
    }

    combinedResponse.getLocationsPerGameObjectType().get(objectType)
        .setLocations(combinedLocations.toArray(new Location[0]));
    return combinedResponse;
  }

  /**
   * Provides a default criteria for the playable locations request.
   *
   * @return an array of Playable Location Criteria
   */
  private Criteria[] getDefaultCriteria() {
    Criteria[] plc = new Criteria[1];
    plc[0] = new Criteria();
    plc[0].setGame_object_type(GAME_OBJECT_TYPE_SPAWN_LOCATIONS);
    plc[0].setFilter(new Filter());
    plc[0].getFilter().setMax_location_count(2);
    plc[0].setFields_to_return(new FieldMask());
    plc[0].getFields_to_return().setPaths(new String[] { "snapped_point", "place_id", "types" });
    return plc;
  }
}
