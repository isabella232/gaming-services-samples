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
import com.google.cloud.firestore.WriteResult;
import com.google.maps.gaming.zoinkies.models.WorldDataRequest;
import com.google.maps.gaming.zoinkies.models.SpawnLocation;
import com.google.maps.gaming.zoinkies.models.WorldData;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLCriteria;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLFieldMask;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLFilter;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLLocation;
import com.google.maps.gaming.zoinkies.models.playablelocations.PLResponse;
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
  GameService GameService;

  /**
   * A reference to the playable locations service
   */
  @Autowired
  PlayableLocationsService PlayableLocationsService;

  /**
   * If it doesn't exist, create one
   * Otherwise return the current user data in the response
   *
   * @param Id The User Id
   * @return A World Data record
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public WorldData GetWorldData(String Id) throws ExecutionException, InterruptedException {
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
  public void SetWorldData(String Id, WorldData worldData)
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
  public WorldData GetSpawnLocations(String Id, WorldDataRequest WorldDataRequest)
      throws Exception {

    Boolean updateNeeded = false;

    WorldData data = GetWorldData(Id);
    if (data == null) {
      data = new WorldData();
    }

    // Query playable locations for the given zone - and only when the overlapping cell
    // isn't in our cache.
    PLResponse response = PlayableLocationsService.RequestPlayableLocations(
        WorldDataRequest.getSouthwest(),
        WorldDataRequest.getNortheast(),
        GetPLDefaultCriteria(),
        data.getS2CellsTTL()
    );

    for (PLLocation plloc:response.getLocationsPerGameObjectType().get("0").getLocations()) {
      // Generate a location key.
      // We use the playable location name as it is unique and always available
      // whether the playable location is generated or not.
      String locationId = plloc.getName().replace("/", "_");

      // If we don't  have this location in the database, generate a new one
      if (!data.getLocations().containsKey(locationId)) {
        // spawn a new location
        SpawnLocation sl = GameService.CreateRandomSpawnLocation(plloc);
        sl.setS2CellId(plloc.getS2CellId());
        data.getLocations().put(locationId, sl);
        updateNeeded = true;
      }
      else {
        // Check the TTL
        String S2CellId = data.getLocations().get(locationId).getS2CellId();
        if (S2CellId != null
            && !S2CellId.isEmpty()
            && data.getS2CellsTTL().containsKey(S2CellId)) {

          Duration duration = Duration.parse(data.getS2CellsTTL().get(S2CellId));
          System.out.println("TTL : " + duration.getSeconds());
          if (duration.getSeconds() <= 0) {
            // Update this location from the world as it has expired
            SpawnLocation sl = GameService.CreateRandomSpawnLocation(plloc);
            sl.setS2CellId(plloc.getS2CellId());
            data.getLocations().put(locationId, sl);
            updateNeeded = true;
          }
        }
      }
    }

      /*
    // If the world does not exist for this user, generate it entirely.
    if (!document.exists()) {
      updateNeeded = true;
      for (PLLocation plloc:response.getLocationsPerGameObjectType().get("0").getLocations()) {
        String locationId = plloc.getName().replace("/","_");
        data.getLocations().put(locationId, GameService.CreateRandomSpawnLocation(plloc));
      }
    }
    else {
      // If the world document is found, synchronize playable locations with existing spawn locations.
      // Randomly generate items for this zone for each placeId not already in the database

      //document.get("locations");
      //data = document.toObject(WorldData.class);

      // Have we already loaded this S2 Cell?
      // If yes, has it expired?
      // If yes, reload
      //if (data.getS2CellsTTL().containsKey())

      // Loop over all found locations
      // If the location is not already in the database, create it
      // Otherwise add it to the combined response
      for (PLLocation plloc:response.getLocationsPerGameObjectType().get("0").getLocations()) {

        String locationId = plloc.getName().replace("/","_");

        if (data != null && data.getLocations() != null
            && !data.getLocations().containsKey(locationId)) {
          // spawn a new location
          data.getLocations().put(locationId, GameService.CreateRandomSpawnLocation(plloc));
          updateNeeded = true;
        }
      }
    }

       */

    // Check if any respawning locations need to be unlocked
    for (SpawnLocation location:data.getLocations().values()) {
      // Check timestamp progress
      if (location.getRespawn_time() != null) {
        Instant t = Instant.parse(location.getRespawn_time());
        if (t.compareTo(Instant.now()) <= 0) {
          location.setRespawn_time(null);
          location.setActive(true);
          updateNeeded = true;
        }
      }
    }

    // Create/Update the world document
    if (updateNeeded) {
      WriteResult writeResult = this.firestore.document("worlds/" + Id).set(data).get();
      System.out.println("Update time: " + writeResult.getUpdateTime());
    }

    // Return the final set
    return data;
  }

  /**
   * Deletes the world locations associated to the player's game.
   * @param Id The User Id
   */
  public void RemoveWorldData(String Id) {
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
  private PLCriteria[] GetPLDefaultCriteria() {
    PLCriteria[] plc = new PLCriteria[1];
    plc[0] = new PLCriteria();
    plc[0].setGame_object_type(0);
    plc[0].setFilter(new PLFilter());
    plc[0].getFilter().setMax_location_count(50);
    plc[0].setFields_to_return(new PLFieldMask());
    plc[0].getFields_to_return().setPaths(new String[]{"snapped_point", "place_id", "types"});
    return plc;
  }
}
