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

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.maps.gaming.zoinkies.models.WorldDataRequest;
import com.google.maps.gaming.zoinkies.models.SpawnLocation;
import com.google.maps.gaming.zoinkies.models.WorldData;
import com.google.maps.gaming.zoinkies.models.playablelocations.Criteria;
import com.google.maps.gaming.zoinkies.models.playablelocations.FieldMask;
import com.google.maps.gaming.zoinkies.models.playablelocations.Filter;
import com.google.maps.gaming.zoinkies.models.playablelocations.Location;
import com.google.maps.gaming.zoinkies.models.playablelocations.Response;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * This class handles the interactions with Firestore to CRUD World Data and Spawn Locations.
 *
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WorldService {

  /**
   * A reference to the Firestore service
   */
  @Autowired
  Firestore firestore;

  /**
   * A reference to the game service
   */
  @Autowired
  GameService gameService;

  /**
   * A reference to the playable locations service
   */
  @Autowired
  PlayableLocationsService playableLocationsService;

  /**
   * If it doesn't exist, create one
   * Otherwise return the current user data in the response
   *
   * @param Id The User Id
   * @return A World Data record
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public WorldData getWorldData(String Id) throws ExecutionException, InterruptedException {
    ApiFuture<DocumentSnapshot> documentSnapshotApiFuture =
        this.firestore.document("worlds/" + Id).get();
    WorldData data = null;
    DocumentSnapshot document = documentSnapshotApiFuture.get();
    if (document.exists()) {
      data = document.toObject(WorldData.class);
    }
    return data;
  }

  /**
   * This class updates the database with an entirely new world data.
   * @param Id The User Id
   * @param worldData The World Data to update
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public void setWorldData(String Id, WorldData worldData)
      throws ExecutionException, InterruptedException {
    ApiFuture<DocumentSnapshot> documentSnapshotApiFuture =
        this.firestore.document("worlds/" + Id).get();
    DocumentSnapshot document = documentSnapshotApiFuture.get();
    if (document.exists()) {
      this.firestore.document("worlds/" + Id).set(worldData).get();
    }
  }

  /**
   * Creates or updates world spawn locations.
   *
   * @param Id  The generated device id identifying the player
   * @param WorldDataRequest The request body (Json format)
   * @return WorldData The updated collection of spawn locations.
   * @throws Exception
   */
  public WorldData getSpawnLocations(String Id, WorldDataRequest WorldDataRequest)
      throws Exception {

    Boolean updateNeeded = false;

    WorldData data = getWorldData(Id);
    if (data == null) {
      data = new WorldData();
    }

    // Query playable locations for the given zone - and only when the overlapping cell
    // isn't in our cache.
    Response response = playableLocationsService.requestPlayableLocations(
        WorldDataRequest.getSouthwest(),
        WorldDataRequest.getNortheast(),
        getDefaultCriteria(),
        data.getS2CellsTTL()
    );

    for (Location plloc:response.getLocationsPerGameObjectType().get("0").getLocations()) {
      // Generate a location key.
      // We use the playable location name as it is unique and always available
      // whether the playable location is generated or not.
      String locationId = plloc.getName().replace("/", "_");

      // If we don't have this location in the database, generate a new one.
      // otherwise we check the time to live field.
      // If the TTL has expired, we create a new spawn location.
      if (!data.getLocations().containsKey(locationId)) {
        SpawnLocation sl = gameService.createRandomSpawnLocation(plloc);
        sl.setS2CellId(plloc.getS2CellId());
        data.getLocations().put(locationId, sl);
        updateNeeded = true;
      }
      else {
        String S2CellId = data.getLocations().get(locationId).getS2CellId();
        if (S2CellId != null
            && !S2CellId.isEmpty()
            && data.getS2CellsTTL().containsKey(S2CellId)) {

          Duration duration = Duration.parse(data.getS2CellsTTL().get(S2CellId));
          if (duration.getSeconds() <= 0) {
            SpawnLocation sl = gameService.createRandomSpawnLocation(plloc);
            sl.setS2CellId(plloc.getS2CellId());
            data.getLocations().put(locationId, sl);
            updateNeeded = true;
          }
        }
      }
    }

    // Check if any respawning locations need to be unlocked.
    for (SpawnLocation location:data.getLocations().values()) {
      // Check timestamp progress.
      if (location.getRespawnTime() != null) {
        Instant t = Instant.parse(location.getRespawnTime());
        if (t.compareTo(Instant.now()) <= 0) {
          location.setRespawnTime(null);
          location.setActive(true);
          updateNeeded = true;
        }
      }
    }

    // Create or Update the world document.
    if (updateNeeded) {
      this.firestore.document("worlds/" + Id).set(data).get();
    }

    // Return the final set
    return data;
  }

  /**
   * Deletes the world locations associated to the player's game.
   * @param Id The User Id
   */
  public void removeWorldData(String Id) {
    CollectionReference users = this.firestore.collection("worlds");
    Iterable<DocumentReference> documentReferences = users.listDocuments();
    documentReferences.forEach(documentReference -> {
      if (documentReference.getId().equals(Id)) {
        try {
          documentReference.delete().get();
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Creates the search criteria for this game.
   *
   * @return a List of Criteria for the query to Playable Locations API
   */
  private Criteria[] getDefaultCriteria() {
    Criteria[] plc = new Criteria[1];
    plc[0] = new Criteria();
    plc[0].setGame_object_type(0);
    plc[0].setFilter(new Filter());
    plc[0].getFilter().setMax_location_count(50);
    plc[0].setFields_to_return(new FieldMask());
    plc[0].getFields_to_return().setPaths(new String[]{"snapped_point", "place_id", "types"});
    return plc;
  }
}
