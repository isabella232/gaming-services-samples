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

  @Autowired
  private com.google.maps.gaming.zoinkies.services.WorldService WorldService;

  @Autowired
  private GameService GameService;

  /**
   * Returns the data associated to the player identified by the given id.
   * @param Id
   * @return
   */
  @GetMapping("/worlds/{id}")
  public ResponseEntity<WorldData> GetWorld(@PathVariable("id") String Id) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    WorldData data = null;

    // Check if this record already exist
    try {
      data = WorldService.GetWorldData(Id);
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
   * @param Id
   * @param LocationId
   * @return
   */
  @PostMapping(path = "/battle/{id}/{locationId}", produces = "application/json")
  public ResponseEntity<BattleData> PostBattle(@PathVariable("id") String Id, @PathVariable("locationId") String LocationId) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty() || LocationId == null || LocationId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    BattleData data;
    try {
      data = GameService.GetBattleData(Id,LocationId);
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

  @PostMapping(path = "/battlesummary/{id}/{locationId}", produces = "application/json")
  public ResponseEntity<BattleSummaryData> PostBattleSummary(@PathVariable("id") String Id,
      @PathVariable("locationId") String LocationId,
      @RequestParam(name = "winner") String Winner) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty() || LocationId == null || LocationId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    BattleSummaryData data;
    try {
      data = GameService.GetBattleSummaryData(Id,LocationId,Boolean.parseBoolean(Winner));
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
   * @param Id
   * @param LocationId
   * @return EnergyData that indicates how much energy is restored. Returns 204 if the station is inactive or respawning.
   */
  @PostMapping(path = "/energystation/{id}/{locationId}", produces = "application/json")
  public ResponseEntity<EnergyData> PostEnergyRestore(@PathVariable("id") String Id, @PathVariable("locationId") String LocationId) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty() || LocationId == null || LocationId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    EnergyData data;
    try {
      data = GameService.GetEnergyStationData(Id,LocationId);
    } catch (LocationStillRespawningException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.ok(data);
  }

  @PostMapping(path = "/chests/{id}/{locationId}", produces = "application/json")
  public ResponseEntity<RewardsData> PostChestRewards(@PathVariable("id") String Id, @PathVariable("locationId") String LocationId) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty() || LocationId == null || LocationId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    RewardsData data = null;
    try {
      data = GameService.GetChestRewards(Id,LocationId);
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
   * @param Id
   * @return
   */
  @PostMapping(path = "/worlds/{id}", consumes = "application/json", produces = "application/json")
  public ResponseEntity<WorldData> GetSpawnLocations(@PathVariable("id") String Id,
      @RequestBody WorldDataRequest WorldDataRequest){

    // The provided Id must be valid
    if (Id == null || Id.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    WorldData data = null;
    try {
      data = WorldService.GetSpawnLocations(Id,WorldDataRequest);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.ok(data);
  }
  /**
   * Deletes everything recorded for the world data associated with the given id.
   * @param Id Generated Device Id identifying the player
   * @return
   */
  @DeleteMapping("/worlds/{id}")
  public ResponseEntity<String> DeleteWorld(@PathVariable("id") String Id) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    WorldService.RemoveWorldData(Id);

    return new ResponseEntity<>(Id, HttpStatus.OK);
  }
}
