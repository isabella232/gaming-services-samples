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
import com.google.maps.gaming.zoinkies.services.PlayerService;
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
 * This class handles all endpoints for the player entity.
 */
@RestController
public class PlayerDataController {

  /**
   * A reference to the player service
   */
  @Autowired
  private PlayerService playerService;

  /**
   * A reference to the game service
   */
  @Autowired
  private GameService gameService;

  /**
   * Returns the data associated to the player identified by the given user id.
   * @param id
   * @return the updated Player Data
   */
  @GetMapping("/users/{id}")
  public ResponseEntity<PlayerData> getUser(@PathVariable("id") String id) {
    // The provided Id must be valid
    if (id == null || id.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    PlayerData data = null;
    // Check if this record already exist
    try {
      data = playerService.GetPlayerData(id);
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
   * @param id the Unique identifier for this player
   * @return the updated Player Data
   */
  @PostMapping(path = "/users/{id}", consumes = "application/json", produces = "application/json")
  public ResponseEntity<PlayerData> createUser(@PathVariable("id") String id,
      @RequestBody(required=false) PlayerData playerData) {
    // The provided Id must be valid
    if (id == null || id.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    // The provided Id must be valid
    // If this is an update, we only update stats
    // Inventory remains as is
    // We also check that items assignments are valid
    // and revert to current value if not
    try {
      if (playerData == null) {
        playerData = gameService.CreateNewUser();
      } else {
        PlayerData current = playerService.GetPlayerData(id);
        if (current != null) {
          playerData.setInventory(current.getInventory());
          if (playerData.getEquippedBodyArmor() != null
              && !playerData.hasInventoryItem(playerData.getEquippedBodyArmor())) {
            playerData.setEquippedBodyArmor(current.getEquippedBodyArmor());
          }
          if (playerData.getEquippedHelmet() != null
              && !playerData.hasInventoryItem(playerData.getEquippedHelmet())) {
            playerData.setEquippedHelmet(current.getEquippedHelmet());
          }
          if (playerData.getEquippedShield() != null
              && !playerData.hasInventoryItem(playerData.getEquippedShield())) {
            playerData.setEquippedShield(current.getEquippedShield());
          }
          if (playerData.getEquippedWeapon() != null
              && !playerData.hasInventoryItem(playerData.getEquippedWeapon())) {
            playerData.setEquippedWeapon(current.getEquippedWeapon());
          }
        }
      }
      playerData = playerService.UpdatePlayerData(id, playerData);
    } catch (ExecutionException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.ok(playerData);
  }

  /**
   * Deletes everything recorded for this player, including cached world data.
   * @param id
   * @return the id of the deleted user
   */
  @DeleteMapping("/users/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
    // The provided Id must be valid
    if (id == null || id.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    playerService.RemoveUserData(id);
    return new ResponseEntity<>(id, HttpStatus.OK);
  }
}