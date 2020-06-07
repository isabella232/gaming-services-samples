package com.google.maps.gaming.zoinkies.playablelocations;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2CellUnion;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import com.google.common.geometry.S2RegionCoverer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * The playable location service provides logic to persist and manipulate world data.
 */
public class PlayableLocationsService {

  private final String PLAYABLE_LOCATION_URL = "https://playablelocations.googleapis.com/v3:samplePlayableLocations";
  private final int S2_CELL_LEVEL = 11;
  private final int S2_CELL_MAX_LEVEL = 14;
  private final String API_KEY = "AIzaSyAZc7i2gq-bv-xYy2Ou2eNDPDUxf5bIQEo";
  private final int GAME_OBJECT_TYPE_SPAWN_LOCATIONS = 0;

  // Singleton pattern
  private static PlayableLocationsService instance;
  public static PlayableLocationsService getInstance() {
    if (instance == null) {
      instance = new PlayableLocationsService();
    }
    return instance;
  }
  private PlayableLocationsService() {}

  /**
   * Loads all playable locations within the S2 cells overlapping with the rectangle area
   * identified by the given north east and south west corners.
   * The search area is potentially way larger from the queried area.
   *
   * @return
   */
  public PLResponse RequestPlayableLocations(PLLatLng loLatLng, PLLatLng hiLatLng, PLCriteria[] criteria) throws Exception {

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("x-goog-api-key", API_KEY);

    // Build query
    PLRequest req = new PLRequest();
    req.areaFilter = new PLAreaFilter();
    if (criteria == null) {
      req.criteria = GetPLDefaultCriteria();
    } else {
      req.criteria = criteria;
    }

    S2RegionCoverer rc = new S2RegionCoverer();
    rc.setMinLevel(this.S2_CELL_LEVEL);
    rc.setMaxLevel(this.S2_CELL_MAX_LEVEL);

    S2LatLng lo = S2LatLng.fromDegrees(loLatLng.latitude,loLatLng.longitude);
    S2LatLng hi = S2LatLng.fromDegrees(hiLatLng.latitude,hiLatLng.longitude);

    S2LatLngRect r = new S2LatLngRect(lo,hi);

    // Get all cells that are covering the provided area
    S2CellUnion cu = rc.getCovering(r);

    PLResponse combinedResponse = new PLResponse();
    String objectType = Integer.toString(GAME_OBJECT_TYPE_SPAWN_LOCATIONS);
    combinedResponse.locationsPerGameObjectType = new HashMap<String, PLLocations>();
    combinedResponse.locationsPerGameObjectType.put(objectType,new PLLocations());

    List<PLLocation> combinedLocations = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    // For each overlapping cell, query playable locations API and merge results into
    // a combined response.
    for (S2CellId id:cu.cellIds()){
      req.areaFilter.s2CellId = Long.toUnsignedString(id.id());

      String reqJson = objectMapper.writeValueAsString(req);
      System.out.println(reqJson);

      HttpEntity<String> request = new HttpEntity<String>(reqJson, headers);
      String plResponse = restTemplate.postForObject(PLAYABLE_LOCATION_URL, request, String.class);
      System.out.println(plResponse);
      assertNotNull(plResponse);

      PLResponse response = objectMapper.readValue(plResponse,PLResponse.class);
      System.out.println("response size" + response.locationsPerGameObjectType.get(objectType).locations.length + " ttl " + response.ttl);
      combinedResponse.ttl = response.ttl;
      combinedLocations.addAll(Arrays.asList(response.locationsPerGameObjectType.get(objectType).locations));
    }

    combinedResponse.locationsPerGameObjectType.get(objectType).locations = combinedLocations.toArray(new PLLocation[0]);
    System.out.println(combinedResponse.locationsPerGameObjectType.get(objectType).locations.length);
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
    plc[0].game_object_type = GAME_OBJECT_TYPE_SPAWN_LOCATIONS;
    plc[0].filter = new PLFilter();
    plc[0].filter.max_location_count = 2;
    plc[0].fields_to_return = new PLFieldMask();
    plc[0].fields_to_return.paths = new String[]{"snapped_point", "place_id", "types"};
    return plc;
  }
}