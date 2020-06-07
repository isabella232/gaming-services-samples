package com.google.maps.gaming.zoinkies.controllers;

import com.google.maps.gaming.zoinkies.services.GameService;
import com.google.maps.gaming.zoinkies.models.PlayerData;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class handles all endpoints for the player entity
 */
@RestController
public class PlayerDataController {

  @Autowired
  private com.google.maps.gaming.zoinkies.services.PlayerService PlayerService;

  @Autowired
  private GameService GameService;

  /**
   * Returns the data associated to the player identified by the given id.
   * @param Id
   * @return
   */
  @GetMapping("/users/{id}")
  public ResponseEntity<PlayerData> GetUser(@PathVariable("id") String Id) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    PlayerData data = null;

    // Check if this record already exist
    try {
      data = PlayerService.GetPlayerData(Id);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    if (data == null) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    return ResponseEntity.ok(data);
  }

  /**
   * Creates or updates a player data record.
   *
   * @param Id the Unique identifier for this player
   * @return the updated Player Data
   */
  @PostMapping(path = "/users/{id}", consumes = "application/json", produces = "application/json")
  public ResponseEntity<PlayerData>  CreateUser(@PathVariable("id") String Id,
      @RequestBody(required=false) PlayerData PlayerData) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    // The provided Id must be valid
    try {

      if (PlayerData == null) {
        PlayerData = GameService.CreateNewUser();
      } else {
        // If this is an update, we only update stats

        PlayerData current = PlayerService.GetPlayerData(Id);
        if (current != null) {
          // Inventory remains as is
          PlayerData.setInventory(current.getInventory());
          // Check that items assignments are valid
          // Revert to current value if not
          if (PlayerData.getEquippedBodyArmor() != null && !PlayerData.hasInventoryItem(PlayerData.getEquippedBodyArmor())) {
            PlayerData.setEquippedBodyArmor(current.getEquippedBodyArmor());
          }
          if (PlayerData.getEquippedHelmet() != null && !PlayerData.hasInventoryItem(PlayerData.getEquippedHelmet())) {
            PlayerData.setEquippedHelmet(current.getEquippedHelmet());
          }
          if (PlayerData.getEquippedShield() != null && !PlayerData.hasInventoryItem(PlayerData.getEquippedShield())) {
            PlayerData.setEquippedShield(current.getEquippedShield());
          }
          if (PlayerData.getEquippedWeapon() != null && !PlayerData.hasInventoryItem(PlayerData.getEquippedWeapon())) {
            PlayerData.setEquippedWeapon(current.getEquippedWeapon());
          }
        }
      }

      PlayerData = PlayerService.UpdatePlayerData(Id, PlayerData);

    } catch (ExecutionException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.ok(PlayerData);
  }

  /**
   * Deletes everything recorded for this player, including cached world data.
   * @param Id
   * @return
   */
  @DeleteMapping("/users/{id}")
  public ResponseEntity<String> DeleteUser(@PathVariable("id") String Id) {

    // The provided Id must be valid
    if (Id == null || Id.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    PlayerService.RemoveUserData(Id);

    return new ResponseEntity<>(Id, HttpStatus.OK);
  }
}