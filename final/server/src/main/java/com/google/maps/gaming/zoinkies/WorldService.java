package com.google.maps.gaming.zoinkies;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.maps.gaming.zoinkies.playablelocations.PLCriteria;
import com.google.maps.gaming.zoinkies.playablelocations.PLFieldMask;
import com.google.maps.gaming.zoinkies.playablelocations.PLFilter;
import com.google.maps.gaming.zoinkies.playablelocations.PLLocation;
import com.google.maps.gaming.zoinkies.playablelocations.PLResponse;
import com.google.maps.gaming.zoinkies.playablelocations.PlayableLocationsService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WorldService {

  @Autowired
  Firestore firestore;

  @Autowired
  GameUtils GameService;

  /**
   * If it doesn't exist, create one
   * Otherwise return the current user data in the response
   *
   * @param Id
   * @return
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public WorldData GetWorldData(String Id) throws ExecutionException, InterruptedException {

    ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = this.firestore.document("worlds/" + Id).get();

    WorldData data = null;
    DocumentSnapshot document = documentSnapshotApiFuture.get();
    if (document.exists()) {
      data = document.toObject(WorldData.class);
      System.out.println("Existing Document: " + document.getData());
    }
    System.out.println("read: " + data);
    return data;
  }

  /**
   *
   * @param Id
   * @param worldData
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public void SetWorldData(String Id, WorldData worldData)
      throws ExecutionException, InterruptedException {

    ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = this.firestore.document("worlds/" + Id).get();

    DocumentSnapshot document = documentSnapshotApiFuture.get();
    if (document.exists()) {
      WriteResult writeResult = this.firestore.document("worlds/" + Id).set(worldData).get();
      System.out.println("Update time: " + writeResult.getUpdateTime());
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
  public WorldData GetSpawnLocations(String Id, WorldDataRequest WorldDataRequest) throws Exception {

    WorldData data = new WorldData();

    // Query playable locations for the given zone
    PLResponse response = PlayableLocationsService.getInstance().RequestPlayableLocations(
        WorldDataRequest.southwest,
        WorldDataRequest.northeast,
        GetPLDefaultCriteria());

    ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = this.firestore.document("worlds/" + Id).get();
    DocumentSnapshot document = documentSnapshotApiFuture.get();

    Boolean updateNeeded = false;

    // If the world does not exist for this user, generate it entirely.
    if (!document.exists()) {
      updateNeeded = true;

      for (PLLocation plloc:response.locationsPerGameObjectType.get("0").locations) {

        String placeId = (plloc.placeId == null || plloc.placeId.isEmpty())?plloc.plusCode:plloc.placeId;

        //if (plloc.placeId != null && !plloc.placeId.isEmpty()) {
          data.getLocations().put(placeId, GameService.CreateRandomSpawnLocation(plloc));
        //} else if (plloc.plusCode != null && !plloc.plusCode.isEmpty()) { // Handle generated locations
        //  data.getLocations().put(plloc.placeId, GameService.CreateRandomSpawnLocation(plloc));
        //}
      }
    }
    else {
      // If the world document is found, synchronize playable locations with existing spawn locations.
      // Randomly generate items for this zone for each placeId not already in the database
      document.get("locations");

      data = document.toObject(WorldData.class);
      //List<SpawnLocation> newLocations = new ArrayList<>();

      // Loop over all found locations
      // If the location is not already in the database, create it
      // Otherwise add it to the combined response
      for (PLLocation plloc:response.locationsPerGameObjectType.get("0").locations) {
        if (data != null && data.getLocations() != null && !data.getLocations().containsKey(plloc.placeId)) {

          String placeId = (plloc.placeId == null || plloc.placeId.isEmpty())?plloc.plusCode:plloc.placeId;
          // spawn a new location
          data.getLocations().put(placeId, GameService.CreateRandomSpawnLocation(plloc));
          updateNeeded = true;
        }
      }
    }

    if (data == null) {
      throw new Exception("Could not update world data! (data is null)");
    }

    // Check if any respawning locations need to be unlocked
    for (SpawnLocation location:data.getLocations().values()) {
      // Check timestamp progress
      if (location.respawn_time != null) {
        Instant t = Instant.parse(location.respawn_time);
        if (t.compareTo(Instant.now()) <= 0) {
          location.respawn_time = null;
          location.active = true;
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
   * @param Id Device generated Id identifying the player.
   * @implNote This function does not remove the stats and inventory associated to the player.
   */
  public void RemoveWorldData(String Id) {
    CollectionReference users = this.firestore.collection("worlds");
    Iterable<DocumentReference> documentReferences = users.listDocuments();
    documentReferences.forEach(documentReference -> {
      if (documentReference.getId().equals(Id)) {
        try {
          documentReference.delete().get();
          System.out.println("Removed world id: " + Id);
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Creates the search criteria for this game.
   *
   * @return
   */
  private PLCriteria[] GetPLDefaultCriteria() {
    PLCriteria[] plc = new PLCriteria[1];
    plc[0] = new PLCriteria();
    plc[0].game_object_type = 0;
    plc[0].filter = new PLFilter();
    plc[0].filter.max_location_count = 50;
    plc[0].fields_to_return = new PLFieldMask();
    plc[0].fields_to_return.paths = new String[]{"snapped_point", "place_id", "types"};
    return plc;
  }
}
