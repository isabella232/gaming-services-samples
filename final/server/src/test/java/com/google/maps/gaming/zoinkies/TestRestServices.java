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
package com.google.maps.gaming.zoinkies;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.maps.gaming.zoinkies.models.BattleData;
import com.google.maps.gaming.zoinkies.models.BattleSummaryData;
import com.google.maps.gaming.zoinkies.models.EnergyData;
import com.google.maps.gaming.zoinkies.models.Item;
import com.google.maps.gaming.zoinkies.models.PlayerData;
import com.google.maps.gaming.zoinkies.models.RewardsData;
import com.google.maps.gaming.zoinkies.models.SpawnLocation;
import com.google.maps.gaming.zoinkies.models.WorldData;
import com.google.maps.gaming.zoinkies.models.WorldDataRequest;
import com.google.maps.gaming.zoinkies.models.playablelocations.LatLng;
import com.google.maps.gaming.zoinkies.services.*;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

@SpringBootTest
@AutoConfigureMockMvc
public class TestRestServices {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private GameService gameService;
  @Autowired
  private com.google.maps.gaming.zoinkies.services.PlayerService playerService;

  private String deviceId = "1234e91f-ce9c-4fa9-ac52-0f7079ce1234";

  @Test
  public void testReferenceDataRead() throws Exception {
    this.mockMvc.perform(get("/references")).andDo(print()).andExpect(status().isOk());
  }

  @Test
  public void testDataCache() throws Exception {
    deleteWorldData();

    System.out.println("Creating world " + deviceId);
    createWorldData();

    System.out.println("Check cache " + deviceId);
    checkDataCache();

    System.out.println("Deleting world " + deviceId);
    deleteWorldData();

  }

  private void checkDataCache() throws Exception {
    WorldData data = getWorldData();
    Assert.isTrue(data.getS2CellsTTL().size() > 0,
        "There should be a few cell ids in this list.");

    // Loop over all spawn locations and confirm that they have a cellId assigned
    for (SpawnLocation sl: data.getLocations().values()) {
      Assert.isTrue(sl.getS2CellId() != null && !sl.getS2CellId().isEmpty(),
          "Spawn locations must have an associated s2 cellid");
      // Check that the S2Cell Id of this location is in the cache
      Assert.isTrue(data.getS2CellsTTL().containsKey(sl.getS2CellId()),
          "The S2CellId of the spawn location must be in the cache!");
      // Check that the ttl for this location is positive
      Duration duration = Duration.parse(data.getS2CellsTTL().get(sl.getS2CellId()));
      //Duration.ofSeconds();
      Assert.isTrue(duration.getSeconds() > 0,
          "TTL must be positive for this location");
    }
  }

  /**
   * Tests Create, Read, Update, and Delete for World Data
   * @throws Exception
   */
  @Test
  public void testWorldDataCRUD() throws Exception {
    // 1 Create
    System.out.println("Creating world " + deviceId);
    createWorldData();
    // 2 Read
    System.out.println("Reading world " + deviceId);
    getWorldData();
    // 4 Delete
    System.out.println("Deleting world " + deviceId);
    deleteWorldData();
  }
  @Test
  public void testEnergyStation() throws Exception {
    // 0 - Delete User and World data in case of leftovers from previous failures
    deleteWorldData();
    deletePlayerData();

    // 1 Create User and World data
    createPlayerData("Johnny");
    createWorldData();

    // 2 Get some battle damage
    PlayerData playerData = getPlayerData("Johnny");
    playerData.setEnergyLevel(5);
    playerService.updatePlayerData(deviceId, playerData);

    // 3 Restore energy - search for a station
    // Search for a chest in our spawn locations
    WorldData data = getWorldData();
    SpawnLocation location = null;
    for (String locationId:data.getLocations().keySet()) {
      if (data.getLocations().get(locationId).getObjectTypeId()
          .equals(GameConstants.ENERGY_STATION)) {
        location = data.getLocations().get(locationId);
        break;
      }
    }
    EnergyData energy = getEnergyData(location.getLocationId());
    Assert.isTrue(energy.getAmountRestored() == 75,
        "Restore amount is incorrect.");

    // Cleanup
    deleteWorldData();
    deletePlayerData();
  }

  @Test
  public void testGeneralBattlePlayerWinsGame() throws Exception {
    // 0 - Delete User and World data in case of leftovers from previous failures
    deleteWorldData();
    deletePlayerData();

    // 1 Create User and World data
    createPlayerData("Johnny");
    createWorldData();

    // Battle minions - search for a minion
    WorldData data = getWorldData();
    SpawnLocation location = null;
    for (String locationId:data.getLocations().keySet()) {
      if (data.getLocations().get(locationId).getObjectTypeId().equals(GameConstants.TOWER)) {
        location = data.getLocations().get(locationId);
        break;
      }
    }

    // Add enough freed leader so we are short of one to win the game
    PlayerData PlayerData = playerService.getPlayerData(deviceId);
    PlayerData.addInventoryItem(new Item(GameConstants.FREED_LEADERS,
        GameConstants.FREED_LEADERS_TO_WIN-1));
    PlayerData.addInventoryItem(new Item(GameConstants.DIAMOND_KEY,5));
    playerService.updatePlayerData(deviceId,PlayerData);

    BattleData battleData = getBattleData(location.getLocationId());
    System.out.println(battleData);
    Assert.isTrue(battleData.getOpponentTypeId().equals(GameConstants.GENERAL),
        "The opponent should be general.");

    Boolean winner = true; // Player
    BattleSummaryData BattleSummaryData = getBattleSummaryData(location.getLocationId(), winner);
    System.out.println(BattleSummaryData);
    Assert.isTrue(BattleSummaryData.getWinner() == winner,
        "The opponent should be the player.");

    Item key = null;
    for (Item item: BattleSummaryData.getRewards().getItems()) {
      if (item.getItemId().equals(GameConstants.FREED_LEADERS)) {
        key = item;
        break;
      }
    }

    Assert.isTrue(key != null && key.getQuantity()==1,
        "The player should have freed one leader.");
    Assert.isTrue(BattleSummaryData.getWonTheGame(),
        "The player should have won the game.");

    // Cleanup
    deleteWorldData();
    deletePlayerData();
  }

  @Test
  public void testMinionBattleMinionWins() throws Exception {
    // 0 - Delete User and World data in case of leftovers from previous failures
    deleteWorldData();
    deletePlayerData();

    // 1 Create User and World data
    createPlayerData("Johnny");
    createWorldData();

    // Battle minions - search for a minion
    WorldData data = getWorldData();
    SpawnLocation location = null;
    for (String locationId:data.getLocations().keySet()) {
      if (data.getLocations().get(locationId).getObjectTypeId().equals(GameConstants.MINION)) {
        location = data.getLocations().get(locationId);
        break;
      }
    }

    // Add a gold key - which will be lost
    PlayerData PlayerData = playerService.getPlayerData(deviceId);
    PlayerData.addInventoryItem(new Item(GameConstants.GOLD_KEY,1));
    playerService.updatePlayerData(deviceId,PlayerData);

    BattleData battleData = getBattleData(location.getLocationId());
    System.out.println(battleData);
    Assert.isTrue(battleData.getOpponentTypeId().equals(GameConstants.MINION),
        "The opponent should be minion.");

    Boolean winner = false; // Minion
    BattleSummaryData BattleSummaryData = getBattleSummaryData(location.getLocationId(), winner);
    System.out.println(BattleSummaryData);
    Assert.isTrue(BattleSummaryData.getWinner() == winner,
        "The winner should be minion.");
    List<Item> items = BattleSummaryData.getRewards().getItems();

    Item key = null;
    for (Item item: items) {
      if (item.getItemId().equals(GameConstants.GOLD_KEY)) {
        key = item;
        break;
      }
    }
    Assert.isTrue(key != null && key.getQuantity()==-1,
        "The player should have lost one gold key.");

    // Cleanup
    deleteWorldData();
    deletePlayerData();
  }

  @Test
  public void testMinionBattleMinionLoses() throws Exception {
    // 0 - Delete User and World data in case of leftovers from previous failures
    deleteWorldData();
    deletePlayerData();

    // 1 Create User and World data
    createPlayerData("Johnny");
    createWorldData();

    // Battle minions - search for a minion
    WorldData data = getWorldData();
    SpawnLocation location = null;
    for (String locationId:data.getLocations().keySet()) {
      if (data.getLocations().get(locationId).getObjectTypeId().equals(GameConstants.MINION)) {
        location = data.getLocations().get(locationId);
        break;
      }
    }
    BattleData battleData = getBattleData(location.getLocationId());
    System.out.println(battleData);
    Assert.isTrue(battleData.getOpponentTypeId().equals(GameConstants.MINION),
        "The opponent should be minion.");

    Boolean winner = true; // Player
    BattleSummaryData BattleSummaryData = getBattleSummaryData(location.getLocationId(), winner);
    System.out.println(BattleSummaryData);
    Assert.isTrue(BattleSummaryData.getWinner() == winner,
        "The opponent should be minion.");

    Item key = null;
    for (Item item: BattleSummaryData.getRewards().getItems()) {
      if (item.getItemId().equals(GameConstants.GOLD_KEY)) {
        key = item;
        break;
      }
    }
    Assert.isTrue(key != null && key.getQuantity()==1,
        "The player should have won one gold key.");

    // Cleanup
    deleteWorldData();
    deletePlayerData();
  }

  @Test
  public void testChestRewardsWithRespawing() throws Exception {
    // 0 - Delete User and World data in case of leftovers from previous failures
    deleteWorldData();
    deletePlayerData();

    // 1 Create User and World data
    createPlayerData("Johnny");
    createWorldData();

    // 2 Get World and Player Data
    // Grant ourselves a few gold keys to unlock the chest
    PlayerData playerData = getPlayerData("Johnny");
    playerData.getInventory().add(new Item(GameConstants.GOLD_KEY,10));
    playerService.updatePlayerData(deviceId, playerData);

    // Search for a chest in our spawn locations
    WorldData data = getWorldData();
    SpawnLocation chest = null;
    for (String locationId:data.getLocations().keySet()) {
      if (data.getLocations().get(locationId).getObjectTypeId().equals(GameConstants.CHEST)) {
        chest = data.getLocations().get(locationId);
        break;
      }
    }

    if (chest != null) {
      System.out.println("Trying to activate Chest at locationId: " + chest.getLocationId());
      RewardsData rewards = getChestRewards(chest.getLocationId());
      Boolean haveKey = false;
      // Search for a diamond key
      for (Item i:rewards.getItems()) {
        if (i.getItemId().equals(GameConstants.DIAMOND_KEY)) {
          haveKey = true;
          break;
        }
      }
      Assert.isTrue(haveKey, "Diamond Key not found in chest rewards!");

      // Try to open the chest a 2nd time
      System.out.println("Trying to activate Chest at locationId: " + chest.getLocationId() + " a 2nd time");
      getRespawingChest(chest.getLocationId());

    } else {
      System.out.println("No chest in all spawned locations? Unlucky we are, aren't we?");
    }

    // Delete User and World data
    deleteWorldData();
    deletePlayerData();

  }

    /**
     * Tests the activation of a chest and the generation of rewards.
     *
     * @throws Exception
     */
  @Test
  public void testChestRewards() throws Exception {
    // 0 - Delete User and World data in case of leftovers from previous failures
    deleteWorldData();
    deletePlayerData();

    // 1 Create User and World data
    createPlayerData("Johnny");
    createWorldData();

    // 2 Get World and Player Data
    // Grant ourselves a few gold keys to unlock the chest
    PlayerData playerData = getPlayerData("Johnny");
    playerData.getInventory().add(new Item(GameConstants.GOLD_KEY,10));
    playerService.updatePlayerData(deviceId, playerData);

    WorldData data = getWorldData();
    // Search for a chest in our spawn locations
    SpawnLocation chest = null;
    for (String locationId:data.getLocations().keySet()) {
      if (data.getLocations().get(locationId).getObjectTypeId().equals(GameConstants.CHEST)) {
        chest = data.getLocations().get(locationId);
        break;
      }
    }
    if (chest != null) {
      System.out.println("Trying to activate Chest at locationId: " + chest.getLocationId());
      RewardsData rewards = getChestRewards(chest.getLocationId());
      Boolean haveKey = false;
      // Search for a diamond key
      for (Item i:rewards.getItems()) {
        if (i.getItemId().equals(GameConstants.DIAMOND_KEY)) {
          haveKey = true;
          break;
        }
      }
      Assert.isTrue(haveKey, "Diamond Key not found in chest rewards!");

      // Assert that there is now a diamond key in the player's inventory
      playerData = getPlayerData("Johnny");
      haveKey = false;
      for (Item i:playerData.getInventory()) {
        if (i.getItemId().equals(GameConstants.DIAMOND_KEY)) {
          haveKey = true;
          break;
        }
      }
      Assert.isTrue(haveKey, "Diamond Key not found in player's inventory!");

    } else {
      System.out.println("No chest in all spawned locations? Unlucky we are, aren't we?");
    }

    // Delete User and World data
    deleteWorldData();
    deletePlayerData();
  }

  /**
   * Run all tests in order.
   * We create a record,
   * verify that we can read it,
   * Update it and finally remove it.
   *
   * @throws Exception
   */
  @Test
  public void testPlayerDataCRUD() throws Exception{
    // 1 Create
    System.out.println("Creating record John Doe");
    createPlayerData("John Doe");
    // 2 Read
    System.out.println("Reading record John Doe");
    getPlayerData("John Doe");
    // 3 Update
    System.out.println("Updating record to Toto");
    createPlayerData("Toto");
    // 4 Delete
    System.out.println("Deleting record Toto");
    deletePlayerData();
  }

  // region util functions

  /**
   * Returns the world data for the test user
   * @throws Exception
   */
  private WorldData getWorldData() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    MvcResult result = this.mockMvc.perform(get("/worlds/{id}", deviceId)).andDo(print())
        .andExpect(status().isOk()).andReturn();
    WorldData data = objectMapper.readValue(result.getResponse().getContentAsString(),
        WorldData.class);

    return data;
  }

  /**
   * Creates or updates the world data for the test user
   * @throws Exception
   */
  private void createWorldData() throws Exception {
    WorldDataRequest request = createWorldDataRequest();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    String json = objectMapper.writeValueAsString(request);
    this.mockMvc.perform(post("/worlds/{id}", deviceId)
        .contentType("application/json").content(json)).andDo(print()).andExpect(status().isOk());
  }
  /**
   * Delete the world data for the test user
   * @throws Exception
   */
  private void deleteWorldData() throws Exception {
    this.mockMvc.perform(delete("/worlds/{id}", deviceId)).andDo(print()).andExpect(status()
        .isOk());
  }

  /**
   *
   * @throws Exception
   */
  private void getRespawingChest(String locationId) throws Exception {

    this.mockMvc.perform(post("/chests/{id}/{locationId}", deviceId,locationId))
        .andDo(print())
        .andExpect(status()
            .isNoContent());
  }

  /**
   *
   * @throws Exception
   */
  private BattleData getBattleData(String locationId) throws Exception {

    MvcResult results = this.mockMvc.perform(post("/battle/{id}/{locationId}", deviceId,
        locationId))
        .andDo(print())
        .andExpect(status()
            .isOk())
        .andReturn();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    BattleData data = objectMapper.readValue(results.getResponse().getContentAsString(),
        BattleData.class);

    return data;
  }

  /**
   * Invokes the battlesummmary REST Api
   * @param locationId
   * @return
   * @throws Exception
   */
  private BattleSummaryData getBattleSummaryData(String locationId, Boolean winnerIs)
      throws Exception {
    MvcResult results = this.mockMvc.perform(post("/battlesummary/{id}/{locationId}",
        deviceId,locationId).param("winner",winnerIs.toString()))
        .andDo(print())
        .andExpect(status()
            .isOk())
        .andReturn();

    ObjectMapper objectMapper = new ObjectMapper();
    BattleSummaryData data = objectMapper.readValue(results.getResponse().getContentAsString(),
        BattleSummaryData.class);
    return data;
  }

  /**
   *
   * @throws Exception
   */
  private EnergyData getEnergyData(String locationId) throws Exception {

    MvcResult results = this.mockMvc.perform(post("/energystation/{id}/{locationId}",
        deviceId,locationId))
        .andDo(print())
        .andExpect(status()
            .isOk())
        .andReturn();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    EnergyData data = objectMapper.readValue(results.getResponse().getContentAsString(),
        EnergyData.class);

    return data;
  }

  /**
   *
   * @throws Exception
   */
  private RewardsData getChestRewards(String locationId) throws Exception {

    MvcResult results = this.mockMvc.perform(post("/chests/{id}/{locationId}",
        deviceId,locationId))
        .andDo(print())
        .andExpect(status()
            .isOk())
        .andReturn();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    RewardsData data = objectMapper.readValue(results.getResponse().getContentAsString(),
        RewardsData.class);

    return data;
  }

  /**
   * Get the player's data - make sure the given name is found in the record
   * @param newName new player name
   * @throws Exception
   */
  private PlayerData getPlayerData(String newName) throws Exception {
    MvcResult results = this.mockMvc.perform(get("/users/{id}", deviceId)).andDo(print()).
        andExpect(status().isOk())
        .andExpect(content().string(containsString(newName))).andReturn();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    PlayerData data = objectMapper.readValue(results.getResponse().getContentAsString(),
        PlayerData.class);
    return data;
  }

  /**
   * Update the player's data - update the name attribute of the record
   * @throws Exception
   */
  private void createPlayerData(String newName) throws Exception {
    PlayerData newData = gameService.createNewUser();
    newData.setName(newName);

    String json = new ObjectMapper().writeValueAsString(newData);
    this.mockMvc.perform(post("/users/{id}", deviceId)
        .contentType("application/json").content(json)).andDo(print()).andExpect(status().isOk());
  }

  /**
   * Delete the player's data
   * @throws Exception
   */
  private void deletePlayerData() throws Exception {
    this.mockMvc.perform(delete("/users/{id}", deviceId)).andDo(print()).andExpect(status()
        .isOk());
  }

  /**
   * Util function that returns a world data request.
   * @return
   */
  private WorldDataRequest createWorldDataRequest() {
    WorldDataRequest request = new WorldDataRequest();
    request.setNortheast(new LatLng(48.8583701, 2.2944813));
    request.setSouthwest(new LatLng(48.8446743,2.2488194));
    return request;
  }
  // endregion
}
