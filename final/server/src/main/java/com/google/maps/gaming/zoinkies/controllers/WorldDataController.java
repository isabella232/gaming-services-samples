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

package com.google.maps.gaming.zoinkies.controllers;

import com.google.maps.gaming.zoinkies.services.GameService;
import com.google.maps.gaming.zoinkies.services.WorldService;
import com.google.maps.gaming.zoinkies.exceptions.LocationStillRespawningException;
import com.google.maps.gaming.zoinkies.exceptions.NotEnoughResourcesToUnlockException;
import com.google.maps.gaming.zoinkies.models.BattleData;
import com.google.maps.gaming.zoinkies.models.BattleSummaryData;
import com.google.maps.gaming.zoinkies.models.EnergyData;
import com.google.maps.gaming.zoinkies.models.RewardsData;
import com.google.maps.gaming.zoinkies.models.WorldData;
import com.google.maps.gaming.zoinkies.models.WorldDataRequest;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class handles all endpoints for world data and spawn locations.
 */
@RestController
public class WorldDataController {

  /**
   * A reference to the world service
   */
  @Autowired
  private WorldService worldService;

  /**
   * A reference to the game service
   */
  @Autowired
  private GameService gameService;

  /**
   * Returns the data associated to the player identified by the given id.
   * @param id The User Id
   * @return The World Data
   */
  @GetMapping("/worlds/{id}")
  public ResponseEntity<WorldData> getWorld(@PathVariable("id") String id) {
    // The provided Id must be valid
    if (id == null || id.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    WorldData data = null;
    // Check if this record already exist
    try {
      data = worldService.GetWorldData(id);
    } catch (ExecutionException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (InterruptedException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.ok(data);
  }

  /**
   * Should be invoked when a battle starts.
   * The call provides all extra parameters for the upcoming battle.
   * More specifically who starts, and any bonuses that the NPC might have.
   *
   * @param id The User Id
   * @param locationId The location Id
   * @return The Battle Data
   */
  @PostMapping(path = "/battle/{id}/{locationId}", produces = "application/json")
  public ResponseEntity<BattleData> postBattle(@PathVariable("id") String id,
      @PathVariable("locationId") String locationId) {
    // The provided Id must be valid
    if (id == null || id.isEmpty() || locationId == null || locationId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    BattleData data;
    try {
      data = gameService.GetBattleData(id,locationId);
    } catch (NotEnoughResourcesToUnlockException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (LocationStillRespawningException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.ok(data);
  }

  /**
   * This entry point is triggered by the client at the end of a battle.
   * The call generates the outcome of the battle depending on the provided winner data.
   * Note that in a production environment, there would be additional checks here
   * to validate the legitimacy of the battle.
   * @param id The User Id
   * @param locationId The Location Id
   * @param winner Winner information: 1-Player wins 0-NPC wins
   * @return A Battle Summary Data with the rewards/penalties for winning/losing.
   */
  @PostMapping(path = "/battlesummary/{id}/{locationId}", produces = "application/json")
  public ResponseEntity<BattleSummaryData> postBattleSummary(@PathVariable("id") String id,
      @PathVariable("locationId") String locationId,
      @RequestParam(name = "winner") String winner) {
    // The provided Id must be valid
    if (id == null || id.isEmpty() || locationId == null || locationId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    BattleSummaryData data;
    try {
      data = gameService.GetBattleSummaryData(id,locationId,Boolean.parseBoolean(winner));
    }catch (LocationStillRespawningException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.ok(data);
  }

  /**
   * Updates the energy of the player based on the current properties of the energy station,
   * and puts the station in respawning state.
   * @param id the user id
   * @param locationId the location id of the energy station
   * @return EnergyData that indicates how much energy is restored.
   * Returns 204 if the station is inactive or respawning.
   */
  @PostMapping(path = "/energystation/{id}/{locationId}", produces = "application/json")
  public ResponseEntity<EnergyData> postEnergyRestore(@PathVariable("id") String id,
      @PathVariable("locationId") String locationId) {
    // The provided Id must be valid
    if (id == null || id.isEmpty() || locationId == null || locationId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    EnergyData data;
    try {
      data = gameService.GetEnergyStationData(id,locationId);
    } catch (LocationStillRespawningException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.ok(data);
  }

  /**
   * This entry point handles the access to the chest resource.
   * A chest rewards items if it can be unlocked.
   *
   * @param id the user id
   * @param locationId the location id of the chest
   * @return A Rewards Data
   */
  @PostMapping(path = "/chests/{id}/{locationId}", produces = "application/json")
  public ResponseEntity<RewardsData> postChestRewards(@PathVariable("id") String id,
      @PathVariable("locationId") String locationId) {
    // The provided Id must be valid
    if (id == null || id.isEmpty() || locationId == null || locationId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    RewardsData data = null;
    try {
      data = gameService.GetChestRewards(id,locationId);
    } catch (NotEnoughResourcesToUnlockException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (LocationStillRespawningException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.ok(data);
  }

  /**
   * Returns the data associated to the player identified by the given id.
   * @param id the user id
   * @return An updated World Data
   */
  @PostMapping(path = "/worlds/{id}", consumes = "application/json", produces = "application/json")
  public ResponseEntity<WorldData> getSpawnLocations(@PathVariable("id") String id,
      @RequestBody WorldDataRequest worldDataRequest){
    // The provided Id must be valid
    if (id == null || id.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    WorldData data = null;
    try {
      data = worldService.GetSpawnLocations(id,worldDataRequest);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.ok(data);
  }
  /**
   * Deletes everything recorded for the world data associated with the given id.
   * @param id Generated Device Id identifying the player
   * @return the id of the deleted world
   */
  @DeleteMapping("/worlds/{id}")
  public ResponseEntity<String> deleteWorld(@PathVariable("id") String id) {
    // The provided Id must be valid
    if (id == null || id.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    worldService.RemoveWorldData(id);
    return new ResponseEntity<>(id, HttpStatus.OK);
  }
}
