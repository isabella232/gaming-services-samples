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
    inventory.add(new Item(GameConstants.BODY_ARMOR_TYPE_1, 1));
    inventory.add(new Item(GameConstants.WEAPON_TYPE_1, 1));
    // Character types are added to the default inventory
    inventory.add(new Item(GameConstants.CHARACTER_TYPE_1, 1));
    inventory.add(new Item(GameConstants.CHARACTER_TYPE_2, 1));
    inventory.add(new Item(GameConstants.CHARACTER_TYPE_3, 1));
    inventory.add(new Item(GameConstants.CHARACTER_TYPE_4, 1));
    PlayerData d = new PlayerData(GameConstants.DEFAULT_PLAYER_NAME,
        GameConstants.CHARACTER_TYPE_1,
        GameConstants.DEFAULT_PLAYER_ENERGY_LEVEL,
        GameConstants.DEFAULT_PLAYER_ENERGY_LEVEL,
        inventory);
    // Equip body armor and weapon starters
    d.setEquippedBodyArmor(GameConstants.BODY_ARMOR_TYPE_1);
    d.setEquippedWeapon(GameConstants.WEAPON_TYPE_1);
    return d;
  }

  /**
   * Helper function that checks and eventually progresses the timestamp of a respawnable location.
   *
   * @param Id  The player's unique identifier
   * @param LocationId The location id
   * @throws Exception
   */
  private void checkLocationStatus(String Id, String LocationId) throws Exception{

    WorldData worldData = worldService.getWorldData(Id);
    if (!worldData.getLocations().containsKey(LocationId)) {
      throw new Exception("Location Id " + LocationId + " not found!");
    }
    SpawnLocation location = worldData.getLocations().get(LocationId);
    // If the location is inactive, progress its timestamp and update its status.
    if (!location.getActive()) {
      if (location.getRespawnTime() == null || location.getRespawnTime().isEmpty()) {
        throw new Exception("Invalid Timestamp found at location " + location.getLocationId());
      }
      // Check timestamp progress
      Instant t = Instant.parse(location.getRespawnTime());
      if (t.compareTo(Instant.now()) > 0) {
        // Still respawning
        throw new LocationStillRespawningException("Location " + LocationId +
            " is still respawning!");
      } else {
        // Reactive the location
        location.setActive(true);
        location.setRespawnTime(null);
        worldService.setWorldData(Id,worldData);
      }
    }
  }

  /**
   * Helper function that sets the duration timestamp on a spawnable location.
   * Respawnable items have a duration set in their config.
   * Updates the World Data.
   * @param ItemId A valid item id
   * @param UserId The player id
   * @param LocationId The location id
   * @param WorldData A reference to the world data
   * @throws Exception
   */
  private void startRespawiningLocation(String ItemId, String UserId, String LocationId,
      WorldData WorldData) throws Exception {
    if (ItemId == null || ItemId.isEmpty())
      throw new Exception("Invalid item Id!");
    if (UserId == null || UserId.isEmpty())
      throw new Exception("Invalid User Id!");
    if (LocationId == null || LocationId.isEmpty())
      throw new Exception("Invalid Location Id!");
    if (WorldData == null)
      throw new Exception("Invalid World Data reference!");
    ReferenceItem refItem = this.getReferenceData().getReferenceItem(ItemId);
    if (refItem == null)
      throw new Exception("Reference item " + ItemId + " not found!");
    if (refItem.getRespawnDuration() != null) {
      SpawnLocation location = WorldData.getLocations().get(LocationId);
      location.setActive(false);
      location.setRespawnTime(Instant.now().plus(refItem.getRespawnDuration()).toString());
      worldService.setWorldData(UserId, WorldData);
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
   * @param Id The unique player id
   * @param LocationId The unique location id
   * @param Winner Who the winner of the battle is (we trust the client in this demo)
   * @return A battle summary data with battle rewards or losses.
   * @throws Exception
   */
  public BattleSummaryData getBattleSummaryData(String Id, String LocationId,
      Boolean Winner) throws Exception {
    WorldData worldData = worldService.getWorldData(Id);
    if (!worldData.getLocations().containsKey(LocationId)) {
      throw new Exception("Location Id " + LocationId + " not found!");
    }
    BattleSummaryData data = new BattleSummaryData();
    data.setWinner(Winner);
    data.setWonTheGame(false);
    SpawnLocation location = worldData.getLocations().get(LocationId);
    if (location.getObjectTypeId().equals(GameConstants.MINION)
        || location.getObjectTypeId().equals(GameConstants.TOWER)) {

      checkLocationStatus(Id,LocationId);

      ReferenceItem refItem = getReferenceData().getReferenceItem(location.getObjectTypeId());
      if (refItem == null) {
        throw new Exception("Can't find reference Item for " + location.getObjectTypeId() + "!");
      }
      PlayerData playerData = playerService.getPlayerData(Id);
      RewardsData rewardsData;

      if (Winner) {
        if (location.getObjectTypeId().equals(GameConstants.MINION)) {
          rewardsData = getRandomMinionBattleRewardsData();
        }
        else {
          rewardsData = getRandomGeneralBattleRewardsData();
        }
        playerData.addAllInventoryItems(rewardsData.getItems());
        List<Item> freedLeaders = playerData.getInventoryItems(GameConstants.FREED_LEADERS);
        if (freedLeaders.size() > 0 && freedLeaders.get(0).getQuantity()
            >= GameConstants.FREED_LEADERS_TO_WIN) {
          data.setWonTheGame(true); // Hurray!
        }
      } else {
        // The player looses a gold key in this case, only if they have one in the inventory.
        rewardsData = new RewardsData();
        List<Item> keys = playerData.getInventoryItems(GameConstants.GOLD_KEY);
        if (keys.size() > 0) {
          int newQty = keys.get(0).getQuantity()-1;
          if (newQty == 0) {
            keys.remove(0);
          } else {
            keys.get(0).setQuantity(newQty);
          }
          rewardsData.getItems().add(new Item(GameConstants.GOLD_KEY,-1));
        }
      }
      data.setRewards(rewardsData);
      data.getRewards().setLocationId(LocationId);
      playerService.updatePlayerData(Id,playerData);
      if (refItem.getRespawnDuration() != null) {
        location.setActive(false);
        location.setRespawnTime(Instant.now().plus(refItem.getRespawnDuration()).toString());
        worldService.setWorldData(Id, worldData);
      }
    }
    return data;
  }

  /**
   * This function handles the logic for the battle setup.
   * More specifically, it checks that all pre-requisites are checked for either engaging minions
   * or attacking a tower.
   *
   * @param Id The userId
   * @param LocationId The LocationId
   * @return A Battle Data
   * @throws Exception
   */
  public BattleData getBattleData(String Id, String LocationId) throws Exception {
    WorldData worldData = worldService.getWorldData(Id);
    if (!worldData.getLocations().containsKey(LocationId)) {
      throw new Exception("Location Id " + LocationId + " not found!");
    }
    // Minion or General?
    // Minion - regenerate - grant gold keys
    // General/towers:
    //  - no regeneration
    //  - grants leaders
    //  - tower disappears afterwards
    //  - requires diamond keys
    BattleData data = new BattleData();;
    data.setLocationId(LocationId);
    SpawnLocation location = worldData.getLocations().get(LocationId);
    if (location.getObjectTypeId().equals(GameConstants.MINION)) {
      ReferenceItem minionRefItem = getReferenceData().getReferenceItem(GameConstants.MINION);
      if (minionRefItem == null) {
        throw new Exception("Can't find reference data for Minions!");
      }
      checkLocationStatus(Id,LocationId);
      data.setOpponentTypeId(GameConstants.MINION);
      data.setPlayerStarts(ThreadLocalRandom.current().nextBoolean());
      data.setCooldown(getReferenceData().getReferenceItem(GameConstants.MINION).getCooldown());
      data.setEnergyLevel(GameConstants.DEFAULT_MINION_ENERGY_LEVEL);
      data.setMaxAttackScoreBonus(GameConstants.MAX_ATTACK_BONUS_MINION);
      data.setMaxAttackScoreBonus(GameConstants.MAX_DEFENSE_BONUS_MINION);

    } else if (location.getObjectTypeId().equals(GameConstants.TOWER)) {
      ReferenceItem generalRefItem = getReferenceData().getReferenceItem(GameConstants.GENERAL);
      if (generalRefItem == null) {
        throw new Exception("Can't find reference data for General!");
      }
      ReferenceItem towerRefItem = getReferenceData().getReferenceItem(GameConstants.TOWER);
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
      PlayerData playerData = playerService.getPlayerData(Id);
       ReferenceItem ri = getReferenceData().getReferenceItem(GameConstants.DIAMOND_KEY);
      if (ri == null)
        throw new Exception("Reference item " + GameConstants.DIAMOND_KEY + " not found!");
      List<Item> items = playerData.getInventoryItems(GameConstants.DIAMOND_KEY);
      if (items.size() > 0 && items.get(0).getQuantity()
          >= location.getNumberOfKeysToActivate()) {
        items.get(0).setQuantity(items.get(0).getQuantity()
            - location.getNumberOfKeysToActivate());
        location.setNumberOfKeysToActivate(0);
        worldService.setWorldData(Id,worldData);
        playerService.updatePlayerData(Id, playerData);
      } else {
        throw new NotEnoughResourcesToUnlockException("Not enough Diamond keys to unlock tower!");
      }
      data.setOpponentTypeId(GameConstants.GENERAL);
      data.setPlayerStarts(ThreadLocalRandom.current().nextBoolean());
      data.setCooldown(getReferenceData().getReferenceItem(GameConstants.GENERAL).getCooldown());
      data.setEnergyLevel(GameConstants.DEFAULT_GENERAL_ENERGY_LEVEL);
      data.setMaxAttackScoreBonus(GameConstants.MAX_ATTACK_BONUS_GENERAL);
      data.setMaxAttackScoreBonus(GameConstants.MAX_DEFENSE_BONUS_GENERAL);

    } else {
      // Unexpected LocationId ?
      throw new Exception("Battles can only be started against Minions and Towers (Generals)");
    }
    return data;
  }

  /**
   * Returns the energy recovered from the energy station.
   *
   * @param Id Unique player Id
   * @param LocationId Unique location Id for the station
   * @return new Energy Data
   * @throws Exception
   */
  public EnergyData getEnergyStationData(String Id, String LocationId) throws Exception {
    WorldData worldData = worldService.getWorldData(Id);
    if (!worldData.getLocations().containsKey(LocationId)) {
      throw new Exception("Location Id " + LocationId + " not found!");
    }
    // Check pre-requisites:
    // - Chest must be in active mode and not respawning
    // Get World Data
    checkLocationStatus(Id,LocationId);
    // Get PlayerData
    PlayerData playerData = playerService.getPlayerData(Id);
    int energy = playerData.getMaxEnergyLevel() - playerData.getEnergyLevel();
    playerData.setEnergyLevel(playerData.getMaxEnergyLevel());
    // Refill all energy points
    EnergyData data = new EnergyData();
    data.setLocationId(LocationId);
    data.setAmountRestored(energy);
    // Update Player Data
    playerService.updatePlayerData(Id, playerData);
    // Change station status and start respawning
    startRespawiningLocation(GameConstants.ENERGY_STATION, Id, LocationId, worldData);
    return data;
  }

  /**
   * Returns the rewards associated with the given chest location,
   * after checking that the pre-requisites are met and that the location is active.
   * Starts the respawning of the location afterwards.
   * Also updates the player's inventory in the process.
   * @param Id
   * @param LocationId
   * @return
   * @throws Exception
   */
  public RewardsData getChestRewards(String Id, String LocationId) throws Exception {
    // Check pre-requisites:
    // - Chest must be in active mode and not respawning
    // Get World Data
    WorldData worldData = worldService.getWorldData(Id);
    if (!worldData.getLocations().containsKey(LocationId)) {
      throw new Exception("Location Id " + LocationId + " not found!");
    }
    checkLocationStatus(Id,LocationId);
    SpawnLocation location = worldData.getLocations().get(LocationId);
    // Get PlayerData
    PlayerData playerData = playerService.getPlayerData(Id);
    // - Player must have enough gold keys
    ReferenceData referenceData = getReferenceData();
    ReferenceItem ri = referenceData.getReferenceItem(GameConstants.GOLD_KEY);
    if (ri == null)
      throw new Exception("Reference item " + GameConstants.GOLD_KEY + " not found!");
    ReferenceItem chestRefItem = referenceData.getReferenceItem(GameConstants.CHEST);
    if (chestRefItem == null)
      throw new Exception("Reference item " + GameConstants.CHEST + " not found!");
    // Check if we have enough keys
    List<Item> items = playerData.getInventoryItems(GameConstants.GOLD_KEY);
    if (items.size() > 0
        && items.get(0).getQuantity() >= location.getNumberOfKeysToActivate()) {
      // Consume gold keys and Update player's inventory
      items.get(0).setQuantity(items.get(0).getQuantity()
          - location.getNumberOfKeysToActivate());

      // Start respawning
      startRespawiningLocation(GameConstants.CHEST, Id, LocationId, worldData);
     // Return response
    } else {
      throw new NotEnoughResourcesToUnlockException("Not enough Gold keys to unlock chest!");
    }

    // Get rewards
    // Generate rewards from loot table
    RewardsData data = getRandomChestRewardsData();
    data.setLocationId(Id);

    // Update player's inventory
    for (Item i:data.getItems()) {
      playerData.addInventoryItem(i);
    }

    // Update Player Data
    playerService.updatePlayerData(Id, playerData);

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
     * @return
     */
  public SpawnLocation createRandomSpawnLocation(Location loc) throws Exception {

    if (loc == null) {
      throw new Exception("Invalid location data found while creating random spawn location!");
    }

    String locationId = null;
    if (loc.getName() != null && !loc.getName().isEmpty()) {
      locationId = loc.getName().replace("/","_");
    }
    else {
      throw new Exception("Invalid location name found while creating random spawn location!");
    }

    if (loc.getSnappedPoint() == null && loc.getCenterPoint() == null) {
      throw new Exception(
          "Invalid Lat Lng coordinates found while creating random spawn location!");
    }

    LatLng point = loc.getSnappedPoint()==null?loc.getCenterPoint():loc.getSnappedPoint();

    SpawnLocation location = new SpawnLocation();
    location.setSnappedPoint(point);
    location.setLocationId(locationId);
    int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
    if (isBetween(randomNum, 0,4)) {
      location.setObjectTypeId(GameConstants.ENERGY_STATION);
      location.setActive(true);
      location.setNumberOfKeysToActivate(0);
      location.setKeyTypeId(null);
      location.setRespawns(true);
    } else if (isBetween(randomNum, 5,24)) {
      location.setObjectTypeId(GameConstants.CHEST);
      location.setActive(true);
      location.setNumberOfKeysToActivate(3);
      location.setKeyTypeId(GameConstants.GOLD_KEY);
      location.setRespawns(true);
    } else if (isBetween(randomNum, 25,39)) {
      location.setObjectTypeId( GameConstants.TOWER);
      location.setActive(true);
      location.setNumberOfKeysToActivate(3);
      location.setKeyTypeId(GameConstants.DIAMOND_KEY);
      location.setRespawns(false);
    } else {
      location.setObjectTypeId(GameConstants.MINION);
      location.setActive(true);
      location.setNumberOfKeysToActivate(0);
      location.setKeyTypeId(null);
      location.setRespawns(true);
    }
    return location;
  }

  /**
   * Returns a reference table for bosses battles
   */
  private List<LootRefItem> GeneralBattleLootTable = new ArrayList<LootRefItem>() {
    {
      add(new LootRefItem(GameConstants.GOLD_KEY, 0.4, 1, 1));
      add(new LootRefItem(GameConstants.HELMET_TYPE_2,0.1,1,1));
      add(new LootRefItem(GameConstants.HELMET_TYPE_3,0.05,1,1));
      add(new LootRefItem(GameConstants.BODY_ARMOR_TYPE_2,0.1,1,1));
      add(new LootRefItem(GameConstants.BODY_ARMOR_TYPE_3,0.05,1,1));
      add(new LootRefItem(GameConstants.SHIELD_TYPE_2,0.1,1,1));
      add(new LootRefItem(GameConstants.SHIELD_TYPE_3,0.05,1,1));
      add(new LootRefItem(GameConstants.WEAPON_TYPE_2,0.1,1,1));
      add(new LootRefItem(GameConstants.WEAPON_TYPE_3,0.05,1,1));
    }
  };

  /**
   * Returns a reference table for minion battles
   */
  private List<LootRefItem> MinionBattleLootTable = new ArrayList<LootRefItem>() {
    {
      add(new LootRefItem(GameConstants.HELMET_TYPE_1,0.2,1,1));
      add(new LootRefItem(GameConstants.HELMET_TYPE_2,0.05,1,1));
      add(new LootRefItem(GameConstants.BODY_ARMOR_TYPE_1,0.2,1,1));
      add(new LootRefItem(GameConstants.BODY_ARMOR_TYPE_2,0.05,1,1));
      add(new LootRefItem(GameConstants.SHIELD_TYPE_1,0.2,1,1));
      add(new LootRefItem(GameConstants.SHIELD_TYPE_2,0.05,1,1));
      add(new LootRefItem(GameConstants.WEAPON_TYPE_1,0.2,1,1));
      add(new LootRefItem(GameConstants.WEAPON_TYPE_2,0.05,1,1));
    }
  };

  /**
   * Returns a reference table for chests
   */
  private List<LootRefItem> ChestLootTable = new ArrayList<LootRefItem>(){
    {
      add(new LootRefItem(GameConstants.HELMET_TYPE_1,0.15,1,1));
      add(new LootRefItem(GameConstants.HELMET_TYPE_2,0.07,1,1));
      add(new LootRefItem(GameConstants.HELMET_TYPE_3,0.03,1,1));
      add(new LootRefItem(GameConstants.BODY_ARMOR_TYPE_1,0.15,1,1));
      add(new LootRefItem(GameConstants.BODY_ARMOR_TYPE_2,0.07,1,1));
      add(new LootRefItem(GameConstants.BODY_ARMOR_TYPE_3,0.03,1,1));
      add(new LootRefItem(GameConstants.SHIELD_TYPE_1,0.15,1,1));
      add(new LootRefItem(GameConstants.SHIELD_TYPE_2,0.07,1,1));
      add(new LootRefItem(GameConstants.SHIELD_TYPE_3,0.03,1,1));
      add(new LootRefItem(GameConstants.WEAPON_TYPE_1,0.15,1,1));
      add(new LootRefItem(GameConstants.WEAPON_TYPE_2,0.07,1,1));
      add(new LootRefItem(GameConstants.WEAPON_TYPE_3,0.03,1,1));
    }
  };

  /**
   * With chests you get a gold key and 2 random items on the list.
   * @return
   */
  public RewardsData getRandomMinionBattleRewardsData() throws Exception {
    RewardsData d = new RewardsData();
    d.getItems().add(new Item(GameConstants.GOLD_KEY,1));
    for (int i=0; i<1; i++) {
      d.getItems().add(getRandomLootItem(MinionBattleLootTable));
    }
    return d;
  }

  /**
   * With chests you get a gold key and 2 random items on the list.
   * @return
   */
  public RewardsData getRandomGeneralBattleRewardsData() throws Exception {
    RewardsData d = new RewardsData();
    d.getItems().add(new Item(GameConstants.FREED_LEADERS,1));
    for (int i=0; i<2; i++) {
      d.getItems().add(getRandomLootItem(GeneralBattleLootTable));
    }
    return d;
  }

  /**
   * With chests you get a gold key and 2 random items on the list.
   * @return
   */
  public RewardsData getRandomChestRewardsData() throws Exception {
    RewardsData d = new RewardsData();
    d.getItems().add(new Item(GameConstants.DIAMOND_KEY,1));
    for (int i=0; i<2; i++) {
      d.getItems().add(getRandomLootItem(ChestLootTable));
    }
    return d;
  }

  /**
   * Helper function that returns a random item from a given selection, based on the item weight.
   *
   * @param selections
   * @return A new item
   * @throws Exception
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
   * @param x
   * @param lower
   * @param upper
   * @return
   */
  public boolean isBetween(int x, int lower, int upper) {

    return lower <= x && x <= upper;
  }

}
