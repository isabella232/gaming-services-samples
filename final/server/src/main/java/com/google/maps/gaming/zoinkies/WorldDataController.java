package com.google.maps.gaming.zoinkies;

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

@RestController
public class WorldDataController {

  @Autowired
  private WorldService WorldService;

  @Autowired
  private GameUtils GameService;

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
   * @param PlaceId
   * @return
   */
  @PostMapping(path = "/battle/{id}/{placeId}", produces = "application/json")
  public ResponseEntity<BattleData> PostBattle(@PathVariable("id") String Id, @PathVariable("placeId") String PlaceId) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty() || PlaceId == null || PlaceId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    BattleData data;
    try {
      data = GameService.GetBattleData(Id,PlaceId);
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

  @PostMapping(path = "/battlesummary/{id}/{placeId}", produces = "application/json")
  public ResponseEntity<BattleSummaryData> PostBattleSummary(@PathVariable("id") String Id,
      @PathVariable("placeId") String PlaceId,
      @RequestParam(name = "winner") String Winner) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty() || PlaceId == null || PlaceId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    BattleSummaryData data;
    try {
      data = GameService.GetBattleSummaryData(Id,PlaceId,Boolean.parseBoolean(Winner));
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
   * @param PlaceId
   * @return EnergyData that indicates how much energy is restored. Returns 204 if the station is inactive or respawning.
   */
  @PostMapping(path = "/energystation/{id}/{placeId}", produces = "application/json")
  public ResponseEntity<EnergyData> PostEnergyRestore(@PathVariable("id") String Id, @PathVariable("placeId") String PlaceId) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty() || PlaceId == null || PlaceId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    EnergyData data;
    try {
      data = GameService.GetEnergyStationData(Id,PlaceId);
    } catch (LocationStillRespawningException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.ok(data);
  }

  @PostMapping(path = "/chests/{id}/{placeId}", produces = "application/json")
  public ResponseEntity<RewardsData> PostChestRewards(@PathVariable("id") String Id, @PathVariable("placeId") String PlaceId) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty() || PlaceId == null || PlaceId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    RewardsData data = null;
    try {
      data = GameService.GetChestRewards(Id,PlaceId);
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
