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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.maps.gaming.zoinkies.GameConstants;
import com.google.maps.gaming.zoinkies.ITEMS;
import com.google.maps.gaming.zoinkies.exceptions.LocationStillRespawningException;
import com.google.maps.gaming.zoinkies.exceptions.NotEnoughResourcesToUnlockException;
import com.google.maps.gaming.zoinkies.models.BattleData;
import com.google.maps.gaming.zoinkies.models.BattleSummaryData;
import com.google.maps.gaming.zoinkies.models.EnergyData;
import com.google.maps.gaming.zoinkies.models.Item;
import com.google.maps.gaming.zoinkies.models.LootRefItem;
import com.google.maps.gaming.zoinkies.models.PlayerData;
import com.google.maps.gaming.zoinkies.models.ReferenceData;
import com.google.maps.gaming.zoinkies.models.ReferenceItem;
import com.google.maps.gaming.zoinkies.models.RewardsData;
import com.google.maps.gaming.zoinkies.models.SpawnLocation;
import com.google.maps.gaming.zoinkies.models.WorldData;
import com.google.maps.gaming.zoinkies.models.playablelocations.LatLng;
import com.google.maps.gaming.zoinkies.models.playablelocations.Location;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * This class contains gameplay logic for this demo.
 * Among others, it handles the player's inventory, battle loots or losses,
 * and locations respawn.
 *
 * It leverages services classes such as PlayerService and WorldService.
 * It also uses ReferenceData.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GameService {

  /**
   * A reference to the world service
   */
  @Autowired
  WorldService worldService;

  /**
   * A reference to the player service
   */
  @Autowired
  PlayerService playerService;

  /**
   * Keeps a reference to game data after being loaded from the resources folder.
   */
  private ReferenceData referenceData;

  /**
   * Gives access to cached reference data.
   * Reference data is provided as part of a resource json file.
   *
   * @return A new reference data
   */
  public ReferenceData getReferenceData() {
    if (referenceData == null) {
      String content = "";
      try {
        try (InputStream inputStream = getClass().getResourceAsStream("/ReferenceData.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
          content = reader.lines()
              .collect(Collectors.joining(System.lineSeparator()));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        referenceData = objectMapper.readValue(content, ReferenceData.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return referenceData;
  }

  /**
   * Creates a new PlayerData object
   * @return A new PlayerData.
   */
  public PlayerData createNewUser() {
    // Default Inventory
    List<Item> inventory = new ArrayList<>();
    inventory.add(new Item(ITEMS.BODY_ARMOR_TYPE_1, 1));
    inventory.add(new Item(ITEMS.WEAPON_TYPE_1, 1));
    // Character types are added to the default inventory
    inventory.add(new Item(ITEMS.CHARACTER_TYPE_1, 1));
    inventory.add(new Item(ITEMS.CHARACTER_TYPE_2, 1));
    inventory.add(new Item(ITEMS.CHARACTER_TYPE_3, 1));
    inventory.add(new Item(ITEMS.CHARACTER_TYPE_4, 1));
    PlayerData d = new PlayerData(GameConstants.DEFAULT_PLAYER_NAME,
        ITEMS.CHARACTER_TYPE_1,
        GameConstants.DEFAULT_PLAYER_ENERGY_LEVEL,
        GameConstants.DEFAULT_PLAYER_ENERGY_LEVEL,
        inventory);
    // Equip body armor and weapon starters
    d.setEquippedBodyArmor(ITEMS.BODY_ARMOR_TYPE_1);
    d.setEquippedWeapon(ITEMS.WEAPON_TYPE_1);
    return d;
  }

  /**
   * Helper function that checks and eventually progresses the timestamp of a respawnable location.
   *
   * @param deviceId  The player's unique identifier
   * @param locationId The location id
   * @throws Exception When conditions are not met
   */
  private void checkLocationStatus(String deviceId, String locationId) throws Exception{

    WorldData worldData = worldService.getWorldData(deviceId);
    if (!worldData.getLocations().containsKey(locationId)) {
      throw new Exception("Location Id " + locationId + " not found!");
    }
    SpawnLocation location = worldData.getLocations().get(locationId);
    // If the location is inactive, progress its timestamp and update its status.
    if (!location.getActive()) {
      if (location.getRespawnTime() == null || location.getRespawnTime().isEmpty()) {
        throw new Exception("Invalid Timestamp found at location " + location.getLocationId());
      }
      // Check timestamp progress
      Instant t = Instant.parse(location.getRespawnTime());
      if (t.compareTo(Instant.now()) > 0) {
        // Still respawning
        throw new LocationStillRespawningException("Location " + locationId +
            " is still respawning!");
      } else {
        // Reactive the location
        location.setActive(true);
        location.setRespawnTime(null);
        worldService.setWorldData(deviceId,worldData);
      }
    }
  }

  /**
   * Helper function that sets the duration timestamp on a spawnable location.
   * Respawnable items have a duration set in their config.
   * Updates the World Data.
   * @param itemId A valid item id
   * @param deviceId The player id
   * @param locationId The location id
   * @param worldData A reference to the world data
   * @throws Exception When conditions are not met
   */
  private void startRespawiningLocation(ITEMS itemId, String deviceId, String locationId,
      WorldData worldData) throws Exception {
    if (itemId == null)
      throw new Exception("Invalid item Id!");
    if (deviceId == null || deviceId.isEmpty())
      throw new Exception("Invalid User Id!");
    if (locationId == null || locationId.isEmpty())
      throw new Exception("Invalid Location Id!");
    if (worldData == null)
      throw new Exception("Invalid World Data reference!");
    ReferenceItem refItem = this.getReferenceData().getReferenceItem(itemId);
    if (refItem == null)
      throw new Exception("Reference item " + itemId + " not found!");
    if (refItem.getRespawnDuration() != null) {
      SpawnLocation location = worldData.getLocations().get(locationId);
      location.setActive(false);
      location.setRespawnTime(Instant.now().plus(refItem.getRespawnDuration()).toString());
      worldService.setWorldData(deviceId, worldData);
    }
  }

  /**
   * Returns the summary of the current battle.
   * There is no cheat code detection in this demo, but this could be a location for it.
   * Checks that this location isn't already respawning.
   * Gets the associated reference Item and processes the battle summary.
   * If the Player wins it generates battle rewards.
   * Note that rewards vary based on opponent.
   * Also checks if the game was won.
   * If Zoinkies win, Player loses one key if they have any.
   * Updates the inventory and locks the location as the battle has ended.
   *
   * @param deviceId The unique player id
   * @param locationId The unique location id
   * @param winner Who the winner of the battle is (we trust the client in this demo)
   * @return A battle summary data with battle rewards or losses.
   * @throws Exception When conditions are not met
   */
  public BattleSummaryData getBattleSummaryData(String deviceId, String locationId,
      boolean winner) throws Exception {
    WorldData worldData = worldService.getWorldData(deviceId);
    if (!worldData.getLocations().containsKey(locationId)) {
      throw new Exception("Location Id " + locationId + " not found!");
    }
    BattleSummaryData data = new BattleSummaryData();
    data.setWinner(winner);
    data.setWonTheGame(false);
    SpawnLocation location = worldData.getLocations().get(locationId);
    if (location.getObjectTypeId().equals(ITEMS.MINION)
        || location.getObjectTypeId().equals(ITEMS.TOWER)) {

      checkLocationStatus(deviceId,locationId);

      ReferenceItem refItem = getReferenceData().getReferenceItem(location.getObjectTypeId());
      if (refItem == null) {
        throw new Exception("Can't find reference Item for " + location.getObjectTypeId() + "!");
      }
      PlayerData playerData = playerService.getPlayerData(deviceId);
      RewardsData rewardsData;

      if (winner) {
        if (location.getObjectTypeId().equals(ITEMS.MINION)) {
          rewardsData = getRandomMinionBattleRewardsData();
        }
        else {
          rewardsData = getRandomGeneralBattleRewardsData();
        }
        playerData.addAllInventoryItems(rewardsData.getItems());
        List<Item> freedLeaders = playerData.getInventoryItems(ITEMS.FREED_LEADERS);
        if (freedLeaders.size() > 0 && freedLeaders.get(0).getQuantity()
            >= GameConstants.FREED_LEADERS_TO_WIN) {
          data.setWonTheGame(true); // Hurray!
        }
      } else {
        // The player looses a gold key in this case, only if they have one in the inventory.
        rewardsData = new RewardsData();
        List<Item> keys = playerData.getInventoryItems(ITEMS.GOLD_KEY);
        if (keys.size() > 0) {
          int newQty = keys.get(0).getQuantity()-1;
          if (newQty == 0) {
            keys.remove(0);
          } else {
            keys.get(0).setQuantity(newQty);
          }
          rewardsData.getItems().add(new Item(ITEMS.GOLD_KEY,-1));
        }
      }
      data.setRewards(rewardsData);
      data.getRewards().setLocationId(locationId);
      playerService.updatePlayerData(deviceId,playerData);
      if (refItem.getRespawnDuration() != null) {
        location.setActive(false);
        location.setRespawnTime(Instant.now().plus(refItem.getRespawnDuration()).toString());
        worldService.setWorldData(deviceId, worldData);
      }
    }
    return data;
  }

  /**
   * This function handles the logic for the battle setup.
   * More specifically, it checks that all pre-requisites are checked for either engaging minions
   * or attacking a tower.
   *
   * @param deviceId The unique id for this player
   * @param locationId The locationId
   * @return A Battle Data
   * @throws Exception When conditions are not met
   */
  public BattleData getBattleData(String deviceId, String locationId) throws Exception {
    WorldData worldData = worldService.getWorldData(deviceId);
    if (!worldData.getLocations().containsKey(locationId)) {
      throw new Exception("Location Id " + locationId + " not found!");
    }
    // Minion or General?
    // Minion - regenerate - grant gold keys
    // General/towers:
    //  - no regeneration
    //  - grants leaders
    //  - tower disappears afterwards
    //  - requires diamond keys
    BattleData data = new BattleData();;
    data.setLocationId(locationId);
    SpawnLocation location = worldData.getLocations().get(locationId);
    if (location.getObjectTypeId().equals(ITEMS.MINION)) {
      ReferenceItem minionRefItem = getReferenceData().getReferenceItem(ITEMS.MINION);
      if (minionRefItem == null) {
        throw new Exception("Can't find reference data for Minions!");
      }
      checkLocationStatus(deviceId,locationId);
      data.setOpponentTypeId(ITEMS.MINION);
      data.setPlayerStarts(ThreadLocalRandom.current().nextBoolean());
      data.setCooldown(getReferenceData().getReferenceItem(ITEMS.MINION).getCooldown());
      data.setEnergyLevel(GameConstants.DEFAULT_MINION_ENERGY_LEVEL);
      data.setMaxAttackScoreBonus(GameConstants.MAX_ATTACK_BONUS_MINION);
      data.setMaxAttackScoreBonus(GameConstants.MAX_DEFENSE_BONUS_MINION);

    } else if (location.getObjectTypeId().equals(ITEMS.TOWER)) {
      ReferenceItem generalRefItem = getReferenceData().getReferenceItem(ITEMS.GENERAL);
      if (generalRefItem == null) {
        throw new Exception("Can't find reference data for General!");
      }
      ReferenceItem towerRefItem = getReferenceData().getReferenceItem(ITEMS.TOWER);
      if (towerRefItem == null) {
        throw new Exception("Can't find reference data for Towers!");
      }
      // Check pre-requisites
      // Get PlayerData
      // - Player must have enough gold keys
      // Consume diamond keys
      // If pre-reqs not met, return custom 20X code
      // Update player's inventory
      // Location is unlocked
      // Update location
      // Update Player Data
      PlayerData playerData = playerService.getPlayerData(deviceId);
       ReferenceItem ri = getReferenceData().getReferenceItem(ITEMS.DIAMOND_KEY);
      if (ri == null)
        throw new Exception("Reference item " + ITEMS.DIAMOND_KEY + " not found!");
      List<Item> items = playerData.getInventoryItems(ITEMS.DIAMOND_KEY);
      if (items.size() > 0 && items.get(0).getQuantity()
          >= location.getNumberOfKeysToActivate()) {
        items.get(0).setQuantity(items.get(0).getQuantity()
            - location.getNumberOfKeysToActivate());
        location.setNumberOfKeysToActivate(0);
        worldService.setWorldData(deviceId,worldData);
        playerService.updatePlayerData(deviceId, playerData);
      } else {
        throw new NotEnoughResourcesToUnlockException("Not enough Diamond keys to unlock tower!");
      }
      data.setOpponentTypeId(ITEMS.GENERAL);
      data.setPlayerStarts(ThreadLocalRandom.current().nextBoolean());
      data.setCooldown(getReferenceData().getReferenceItem(ITEMS.GENERAL).getCooldown());
      data.setEnergyLevel(GameConstants.DEFAULT_GENERAL_ENERGY_LEVEL);
      data.setMaxAttackScoreBonus(GameConstants.MAX_ATTACK_BONUS_GENERAL);
      data.setMaxAttackScoreBonus(GameConstants.MAX_DEFENSE_BONUS_GENERAL);

    } else {
      // Unexpected locationId ?
      throw new Exception("Battles can only be started against Minions and Towers (Generals)");
    }
    return data;
  }

  /**
   * Returns the energy recovered from the energy station.
   *
   * @param deviceId Unique player Id
   * @param locationId Unique location Id for the station
   * @return new Energy Data
   * @throws Exception When conditions are not met
   */
  public EnergyData getEnergyStationData(String deviceId, String locationId) throws Exception {
    WorldData worldData = worldService.getWorldData(deviceId);
    if (!worldData.getLocations().containsKey(locationId)) {
      throw new Exception("Location Id " + locationId + " not found!");
    }
    // Check pre-requisites:
    // - Chest must be in active mode and not respawning
    // Get World Data
    checkLocationStatus(deviceId,locationId);
    // Get PlayerData
    PlayerData playerData = playerService.getPlayerData(deviceId);
    int energy = playerData.getMaxEnergyLevel() - playerData.getEnergyLevel();
    playerData.setEnergyLevel(playerData.getMaxEnergyLevel());
    // Refill all energy points
    EnergyData data = new EnergyData();
    data.setLocationId(locationId);
    data.setAmountRestored(energy);
    // Update Player Data
    playerService.updatePlayerData(deviceId, playerData);
    // Change station status and start respawning
    startRespawiningLocation(ITEMS.ENERGY_STATION, deviceId, locationId, worldData);
    return data;
  }

  /**
   * Returns the rewards associated with the given chest location,
   * after checking that the pre-requisites are met and that the location is active.
   * Starts the respawning of the location afterwards.
   * Also updates the player's inventory in the process.
   * @param deviceId The device id identifying the player.
   * @param locationId The location id of the chest.
   * @return The rewards associated to the chest.
   * @throws Exception When conditions are not met.
   */
  public RewardsData getChestRewards(String deviceId, String locationId) throws Exception {
    // Check pre-requisites:
    // - Chest must be in active mode and not respawning
    // Get World Data
    WorldData worldData = worldService.getWorldData(deviceId);
    if (!worldData.getLocations().containsKey(locationId)) {
      throw new Exception("Location Id " + locationId + " not found!");
    }
    checkLocationStatus(deviceId,locationId);
    SpawnLocation location = worldData.getLocations().get(locationId);
    // Get PlayerData
    PlayerData playerData = playerService.getPlayerData(deviceId);
    // - Player must have enough gold keys
    ReferenceData referenceData = getReferenceData();
    ReferenceItem ri = referenceData.getReferenceItem(ITEMS.GOLD_KEY);
    if (ri == null)
      throw new Exception("Reference item " + ITEMS.GOLD_KEY + " not found!");
    ReferenceItem chestRefItem = referenceData.getReferenceItem(ITEMS.CHEST);
    if (chestRefItem == null)
      throw new Exception("Reference item " + ITEMS.CHEST + " not found!");
    // Check if we have enough keys
    List<Item> items = playerData.getInventoryItems(ITEMS.GOLD_KEY);
    if (items.size() > 0
        && items.get(0).getQuantity() >= location.getNumberOfKeysToActivate()) {
      // Consume gold keys and Update player's inventory
      items.get(0).setQuantity(items.get(0).getQuantity()
          - location.getNumberOfKeysToActivate());

      // Start respawning
      startRespawiningLocation(ITEMS.CHEST, deviceId, locationId, worldData);
     // Return response
    } else {
      throw new NotEnoughResourcesToUnlockException("Not enough Gold keys to unlock chest!");
    }

    // Get rewards
    // Generate rewards from loot table
    RewardsData data = getRandomChestRewardsData();
    data.setLocationId(deviceId);

    // Update player's inventory
    for (Item i:data.getItems()) {
      playerData.addInventoryItem(i);
    }

    // Update Player Data
    playerService.updatePlayerData(deviceId, playerData);

    return data;
  }

    /**
     * Creates a spawn location based on a basic random distribution.
     * These locations are persisted for the duration of the game.
     * The distribution is as follows:
     * 5% -> Restore Stations
     * 20% -> Chests
     * 15% -> Towers
     * 60% -> Minions
     *
     * @return A spawn location
     */
  public SpawnLocation createRandomSpawnLocation(Location location) throws Exception {

    if (location == null) {
      throw new Exception("Invalid location data found while creating random spawn location!");
    }

    String locationId = null;
    if (location.getName() != null && !location.getName().isEmpty()) {
      locationId = location.getName().replace("/","_");
    }
    else {
      throw new Exception("Invalid location name found while creating random spawn location!");
    }

    if (location.getSnappedPoint() == null && location.getCenterPoint() == null) {
      throw new Exception(
          "Invalid Lat Lng coordinates found while creating random spawn location!");
    }

    LatLng point = location.getSnappedPoint()==null?location.getCenterPoint():location.getSnappedPoint();

    SpawnLocation spawnLocation = new SpawnLocation();
    spawnLocation.setSnappedPoint(point);
    spawnLocation.setLocationId(locationId);
    int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
    if (isBetween(randomNum, 0,4)) {
      spawnLocation.setObjectTypeId(ITEMS.ENERGY_STATION);
      spawnLocation.setActive(true);
      spawnLocation.setNumberOfKeysToActivate(0);
      spawnLocation.setKeyTypeId(null);
      spawnLocation.setRespawns(true);
    } else if (isBetween(randomNum, 5,24)) {
      spawnLocation.setObjectTypeId(ITEMS.CHEST);
      spawnLocation.setActive(true);
      spawnLocation.setNumberOfKeysToActivate(3);
      spawnLocation.setKeyTypeId(ITEMS.GOLD_KEY);
      spawnLocation.setRespawns(true);
    } else if (isBetween(randomNum, 25,39)) {
      spawnLocation.setObjectTypeId( ITEMS.TOWER);
      spawnLocation.setActive(true);
      spawnLocation.setNumberOfKeysToActivate(3);
      spawnLocation.setKeyTypeId(ITEMS.DIAMOND_KEY);
      spawnLocation.setRespawns(false);
    } else {
      spawnLocation.setObjectTypeId(ITEMS.MINION);
      spawnLocation.setActive(true);
      spawnLocation.setNumberOfKeysToActivate(0);
      spawnLocation.setKeyTypeId(null);
      spawnLocation.setRespawns(true);
    }
    return spawnLocation;
  }

  /**
   * Returns a reference table for bosses battles
   */
  private List<LootRefItem> GeneralBattleLootTable = new ArrayList<LootRefItem>() {
    {
      add(new LootRefItem(ITEMS.GOLD_KEY, 0.4, 1, 1));
      add(new LootRefItem(ITEMS.HELMET_TYPE_2,0.1,1,1));
      add(new LootRefItem(ITEMS.HELMET_TYPE_3,0.05,1,1));
      add(new LootRefItem(ITEMS.BODY_ARMOR_TYPE_2,0.1,1,1));
      add(new LootRefItem(ITEMS.BODY_ARMOR_TYPE_3,0.05,1,1));
      add(new LootRefItem(ITEMS.SHIELD_TYPE_2,0.1,1,1));
      add(new LootRefItem(ITEMS.SHIELD_TYPE_3,0.05,1,1));
      add(new LootRefItem(ITEMS.WEAPON_TYPE_2,0.1,1,1));
      add(new LootRefItem(ITEMS.WEAPON_TYPE_3,0.05,1,1));
    }
  };

  /**
   * Returns a reference table for minion battles
   */
  private List<LootRefItem> MinionBattleLootTable = new ArrayList<LootRefItem>() {
    {
      add(new LootRefItem(ITEMS.HELMET_TYPE_1,0.2,1,1));
      add(new LootRefItem(ITEMS.HELMET_TYPE_2,0.05,1,1));
      add(new LootRefItem(ITEMS.BODY_ARMOR_TYPE_1,0.2,1,1));
      add(new LootRefItem(ITEMS.BODY_ARMOR_TYPE_2,0.05,1,1));
      add(new LootRefItem(ITEMS.SHIELD_TYPE_1,0.2,1,1));
      add(new LootRefItem(ITEMS.SHIELD_TYPE_2,0.05,1,1));
      add(new LootRefItem(ITEMS.WEAPON_TYPE_1,0.2,1,1));
      add(new LootRefItem(ITEMS.WEAPON_TYPE_2,0.05,1,1));
    }
  };

  /**
   * Returns a reference table for chests
   */
  private List<LootRefItem> ChestLootTable = new ArrayList<LootRefItem>(){
    {
      add(new LootRefItem(ITEMS.HELMET_TYPE_1,0.15,1,1));
      add(new LootRefItem(ITEMS.HELMET_TYPE_2,0.07,1,1));
      add(new LootRefItem(ITEMS.HELMET_TYPE_3,0.03,1,1));
      add(new LootRefItem(ITEMS.BODY_ARMOR_TYPE_1,0.15,1,1));
      add(new LootRefItem(ITEMS.BODY_ARMOR_TYPE_2,0.07,1,1));
      add(new LootRefItem(ITEMS.BODY_ARMOR_TYPE_3,0.03,1,1));
      add(new LootRefItem(ITEMS.SHIELD_TYPE_1,0.15,1,1));
      add(new LootRefItem(ITEMS.SHIELD_TYPE_2,0.07,1,1));
      add(new LootRefItem(ITEMS.SHIELD_TYPE_3,0.03,1,1));
      add(new LootRefItem(ITEMS.WEAPON_TYPE_1,0.15,1,1));
      add(new LootRefItem(ITEMS.WEAPON_TYPE_2,0.07,1,1));
      add(new LootRefItem(ITEMS.WEAPON_TYPE_3,0.03,1,1));
    }
  };

  /**
   * With chests you get a gold key and 2 random items on the list.
   * @return The rewards for this minion battle.
   */
  public RewardsData getRandomMinionBattleRewardsData() throws Exception {
    RewardsData d = new RewardsData();
    d.getItems().add(new Item(ITEMS.GOLD_KEY,1));
    for (int i=0; i<1; i++) {
      d.getItems().add(getRandomLootItem(MinionBattleLootTable));
    }
    return d;
  }

  /**
   * With chests you get a gold key and 2 random items on the list.
   * @return The rewards for this boss battle.
   */
  public RewardsData getRandomGeneralBattleRewardsData() throws Exception {
    RewardsData d = new RewardsData();
    d.getItems().add(new Item(ITEMS.FREED_LEADERS,1));
    for (int i=0; i<2; i++) {
      d.getItems().add(getRandomLootItem(GeneralBattleLootTable));
    }
    return d;
  }

  /**
   * With chests you get a gold key and 2 random items on the list.
   * @return The rewards for this chest
   */
  public RewardsData getRandomChestRewardsData() throws Exception {
    RewardsData d = new RewardsData();
    d.getItems().add(new Item(ITEMS.DIAMOND_KEY,1));
    for (int i=0; i<2; i++) {
      d.getItems().add(getRandomLootItem(ChestLootTable));
    }
    return d;
  }

  /**
   * Helper function that returns a random item from a given selection, based on the item weight.
   *
   * @param selections A list of reference items
   * @return A new item
   * @throws Exception When conditions are not met
   */
  private Item getRandomLootItem(List<LootRefItem> selections) throws Exception {
    double rand = ThreadLocalRandom.current().nextDouble(0,1);
    float currentProb = 0f;
    for ( LootRefItem lri : selections) {
      currentProb += lri.getWeight();
      if (rand <= currentProb)
        return new Item(lri.getItemId(),lri.getMinQuantity());
    }
    //will happen if the input's probabilities sums to less than 1
    //throw error here if that's appropriate
    throw new Exception("The sum of all weights from the given loot table is not equal to 1!");
  }

  /**
   * Util function to check if a number is within range.
   * @param x The value to evaluate
   * @param lower The lower bound
   * @param upper The upper bound
   * @return A boolean that indicates if the provided value is within the interval.
   */
  public boolean isBetween(int x, int lower, int upper) {

    return lower <= x && x <= upper;
  }

}
