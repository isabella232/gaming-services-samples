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
import com.google.maps.gaming.zoinkies.models.playablelocations.PLLatLng;
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
  private com.google.maps.gaming.zoinkies.services.GameService GameService;
  @Autowired
  private com.google.maps.gaming.zoinkies.services.PlayerService PlayerService;

  private String Id = "1234e91f-ce9c-4fa9-ac52-0f7079ce1234";

  @Test
  public void TestReferenceDataRead() throws Exception {
    this.mockMvc.perform(get("/references")).andDo(print()).andExpect(status().isOk());
  }

  /**
   * Tests Create, Read, Update, and Delete for World Data
   * @throws Exception
   */
  @Test
  public void TestWorldDataCRUD() throws Exception {
    // 1 Create
    System.out.println("Creating world " + Id);
    CreateWorldData();
    // 2 Read
    System.out.println("Reading world " + Id);
    GetWorldData();
    // 4 Delete
    System.out.println("Deleting world " + Id);
    DeleteWorldData();
  }
  @Test
  public void TestEnergyStation() throws Exception {
    // 0 - Delete User and World data in case of leftovers from previous failures
    DeleteWorldData();
    DeletePlayerData();

    // 1 Create User and World data
    CreatePlayerData("Johnny");
    CreateWorldData();

    // 2 Get some battle damage
    PlayerData playerData = GetPlayerData("Johnny");
    playerData.setEnergyLevel(5);
    PlayerService.UpdatePlayerData(Id, playerData);

    // 3 Restore energy - search for a station
    // Search for a chest in our spawn locations
    WorldData data = GetWorldData();
    SpawnLocation location = null;
    for (String locationId:data.getLocations().keySet()) {
      if (data.getLocations().get(locationId).object_type_id.equals(GameConstants.ENERGY_STATION)) {
        location = data.getLocations().get(locationId);
        break;
      }
    }
    EnergyData energy = GetEnergyData(location.id);
    Assert.isTrue(energy.getAmountRestored() == 75,
        "Restore amount is incorrect.");

    // Cleanup
    DeleteWorldData();
    DeletePlayerData();
  }

  @Test
  public void TestGeneralBattlePlayerWinsGame() throws Exception {
    // 0 - Delete User and World data in case of leftovers from previous failures
    DeleteWorldData();
    DeletePlayerData();

    // 1 Create User and World data
    CreatePlayerData("Johnny");
    CreateWorldData();

    // Battle minions - search for a minion
    WorldData data = GetWorldData();
    SpawnLocation location = null;
    for (String locationId:data.getLocations().keySet()) {
      if (data.getLocations().get(locationId).object_type_id.equals(GameConstants.TOWER)) {
        location = data.getLocations().get(locationId);
        break;
      }
    }

    // Add enough freed leader so we are short of one to win the game
    PlayerData PlayerData = PlayerService.GetPlayerData(Id);
    PlayerData.addInventoryItem(new Item(GameConstants.FREED_LEADERS,
        GameConstants.FREED_LEADERS_TO_WIN-1));
    PlayerData.addInventoryItem(new Item(GameConstants.DIAMOND_KEY,5));
    PlayerService.UpdatePlayerData(Id,PlayerData);

    BattleData battleData = GetBattleData(location.id);
    System.out.println(battleData);
    Assert.isTrue(battleData.getOpponentTypeId().equals(GameConstants.GENERAL),
        "The opponent should be general.");

    Boolean winner = true; // Player
    BattleSummaryData BattleSummaryData = GetBattleSummaryData(location.id, winner);
    System.out.println(BattleSummaryData);
    Assert.isTrue(BattleSummaryData.winner == winner,
        "The opponent should be the player.");

    Item key = null;
    for (Item item: BattleSummaryData.rewards.getItems()) {
      if (item.getId().equals(GameConstants.FREED_LEADERS)) {
        key = item;
        break;
      }
    }

    Assert.isTrue(key != null && key.getQuantity()==1,
        "The player should have freed one leader.");
    Assert.isTrue(BattleSummaryData.wonTheGame, "The player should have won the game.");

    // Cleanup
    DeleteWorldData();
    DeletePlayerData();
  }

  @Test
  public void TestMinionBattleMinionWins() throws Exception {
    // 0 - Delete User and World data in case of leftovers from previous failures
    DeleteWorldData();
    DeletePlayerData();

    // 1 Create User and World data
    CreatePlayerData("Johnny");
    CreateWorldData();

    // Battle minions - search for a minion
    WorldData data = GetWorldData();
    SpawnLocation location = null;
    for (String locationId:data.getLocations().keySet()) {
      if (data.getLocations().get(locationId).object_type_id.equals(GameConstants.MINION)) {
        location = data.getLocations().get(locationId);
        break;
      }
    }

    // Add a gold key - which will be lost
    PlayerData PlayerData = PlayerService.GetPlayerData(Id);
    PlayerData.addInventoryItem(new Item(GameConstants.GOLD_KEY,1));

    BattleData battleData = GetBattleData(location.id);
    System.out.println(battleData);
    Assert.isTrue(battleData.getOpponentTypeId().equals(GameConstants.MINION),
        "The opponent should be minion.");

    Boolean winner = false; // Minion
    BattleSummaryData BattleSummaryData = GetBattleSummaryData(location.id, winner);
    System.out.println(BattleSummaryData);
    Assert.isTrue(BattleSummaryData.winner == winner,
        "The opponent should be minion.");
    List<Item> items = BattleSummaryData.rewards.getItems();

    Item key = null;
    for (Item item: items) {
      if (item.getId().equals(GameConstants.GOLD_KEY)) {
        key = item;
        break;
      }
    }
    Assert.isTrue(key != null && key.getQuantity()==-1,
        "The player should have lost one gold key.");

    // Cleanup
    DeleteWorldData();
    DeletePlayerData();
  }

  @Test
  public void TestMinionBattleMinionLoses() throws Exception {
    // 0 - Delete User and World data in case of leftovers from previous failures
    DeleteWorldData();
    DeletePlayerData();

    // 1 Create User and World data
    CreatePlayerData("Johnny");
    CreateWorldData();

    // Battle minions - search for a minion
    WorldData data = GetWorldData();
    SpawnLocation location = null;
    for (String locationId:data.getLocations().keySet()) {
      if (data.getLocations().get(locationId).object_type_id.equals(GameConstants.MINION)) {
        location = data.getLocations().get(locationId);
        break;
      }
    }
    BattleData battleData = GetBattleData(location.id);
    System.out.println(battleData);
    Assert.isTrue(battleData.getOpponentTypeId().equals(GameConstants.MINION),
        "The opponent should be minion.");

    Boolean winner = true; // Player
    BattleSummaryData BattleSummaryData = GetBattleSummaryData(location.id, winner);
    System.out.println(BattleSummaryData);
    Assert.isTrue(BattleSummaryData.winner == winner,
        "The opponent should be minion.");

    Item key = null;
    for (Item item: BattleSummaryData.rewards.getItems()) {
      if (item.getId().equals(GameConstants.GOLD_KEY)) {
        key = item;
        break;
      }
    }
    Assert.isTrue(key != null && key.getQuantity()==1,
        "The player should have won one gold key.");

    // Cleanup
    DeleteWorldData();
    DeletePlayerData();
  }

  @Test
  public void TestChestRewardsWithRespawing() throws Exception {
    // 0 - Delete User and World data in case of leftovers from previous failures
    DeleteWorldData();
    DeletePlayerData();

    // 1 Create User and World data
    CreatePlayerData("Johnny");
    CreateWorldData();

    // 2 Get World and Player Data
    // Grant ourselves a few gold keys to unlock the chest
    PlayerData playerData = GetPlayerData("Johnny");
    playerData.getInventory().add(new Item(GameConstants.GOLD_KEY,10));
    PlayerService.UpdatePlayerData(Id, playerData);

    // Search for a chest in our spawn locations
    WorldData data = GetWorldData();
    SpawnLocation chest = null;
    for (String locationId:data.getLocations().keySet()) {
      if (data.getLocations().get(locationId).object_type_id.equals(GameConstants.CHEST)) {
        chest = data.getLocations().get(locationId);
        break;
      }
    }

    if (chest != null) {
      System.out.println("Trying to activate Chest at locationId: " + chest.id);
      RewardsData rewards = GetChestRewards(chest.id);
      Boolean haveKey = false;
      // Search for a diamond key
      for (Item i:rewards.getItems()) {
        if (i.getId().equals(GameConstants.DIAMOND_KEY)) {
          haveKey = true;
          break;
        }
      }
      Assert.isTrue(haveKey, "Diamond Key not found in chest rewards!");

      // Try to open the chest a 2nd time
      System.out.println("Trying to activate Chest at locationId: " + chest.id + " a 2nd time");
      GetRespawingChest(chest.id);

    } else {
      System.out.println("No chest in all spawned locations? Unlucky we are, aren't we?");
    }

    // Delete User and World data
    DeleteWorldData();
    DeletePlayerData();

  }

    /**
     * Tests the activation of a chest and the generation of rewards.
     *
     * @throws Exception
     */
  @Test
  public void TestChestRewards() throws Exception {
    // 0 - Delete User and World data in case of leftovers from previous failures
    DeleteWorldData();
    DeletePlayerData();

    // 1 Create User and World data
    CreatePlayerData("Johnny");
    CreateWorldData();

    // 2 Get World and Player Data
    // Grant ourselves a few gold keys to unlock the chest
    PlayerData playerData = GetPlayerData("Johnny");
    playerData.getInventory().add(new Item(GameConstants.GOLD_KEY,10));
    PlayerService.UpdatePlayerData(Id, playerData);

    WorldData data = GetWorldData();
    // Search for a chest in our spawn locations
    SpawnLocation chest = null;
    for (String locationId:data.getLocations().keySet()) {
      if (data.getLocations().get(locationId).object_type_id.equals(GameConstants.CHEST)) {
        chest = data.getLocations().get(locationId);
        break;
      }
    }
    if (chest != null) {
      System.out.println("Trying to activate Chest at locationId: " + chest.id);
      RewardsData rewards = GetChestRewards(chest.id);
      Boolean haveKey = false;
      // Search for a diamond key
      for (Item i:rewards.getItems()) {
        if (i.getId().equals(GameConstants.DIAMOND_KEY)) {
          haveKey = true;
          break;
        }
      }
      Assert.isTrue(haveKey, "Diamond Key not found in chest rewards!");

      // Assert that there is now a diamond key in the player's inventory
      playerData = GetPlayerData("Johnny");
      haveKey = false;
      for (Item i:playerData.getInventory()) {
        if (i.getId().equals(GameConstants.DIAMOND_KEY)) {
          haveKey = true;
          break;
        }
      }
      Assert.isTrue(haveKey, "Diamond Key not found in player's inventory!");

    } else {
      System.out.println("No chest in all spawned locations? Unlucky we are, aren't we?");
    }

    // Delete User and World data
    DeleteWorldData();
    DeletePlayerData();
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
  public void TestPlayerDataCRUD() throws Exception{
    // 1 Create
    System.out.println("Creating record John Doe");
    CreatePlayerData("John Doe");
    // 2 Read
    System.out.println("Reading record John Doe");
    GetPlayerData("John Doe");
    // 3 Update
    System.out.println("Updating record to Toto");
    CreatePlayerData("Toto");
    // 4 Delete
    System.out.println("Deleting record Toto");
    DeletePlayerData();
  }

  // region util functions

  /**
   * Returns the world data for the test user
   * @throws Exception
   */
  private WorldData GetWorldData() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    MvcResult result = this.mockMvc.perform(get("/worlds/{id}",Id)).andDo(print())
        .andExpect(status().isOk()).andReturn();
    WorldData data = objectMapper.readValue(result.getResponse().getContentAsString(),
        WorldData.class);

    return data;
  }

  /**
   * Creates or updates the world data for the test user
   * @throws Exception
   */
  private void CreateWorldData() throws Exception {
    WorldDataRequest request = CreateWorldDataRequest();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    String json = objectMapper.writeValueAsString(request);
    this.mockMvc.perform(post("/worlds/{id}",Id)
        .contentType("application/json").content(json)).andDo(print()).andExpect(status().isOk());
  }
  /**
   * Delete the world data for the test user
   * @throws Exception
   */
  private void DeleteWorldData() throws Exception {
    this.mockMvc.perform(delete("/worlds/{id}",Id)).andDo(print()).andExpect(status()
        .isOk());
  }

  /**
   *
   * @throws Exception
   */
  private void GetRespawingChest(String locationId) throws Exception {

    this.mockMvc.perform(post("/chests/{id}/{locationId}",Id,locationId))
        .andDo(print())
        .andExpect(status()
            .isNoContent());
  }

  /**
   *
   * @throws Exception
   */
  private BattleData GetBattleData(String locationId) throws Exception {

    MvcResult results = this.mockMvc.perform(post("/battle/{id}/{locationId}",Id,
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
  private BattleSummaryData GetBattleSummaryData(String locationId, Boolean winnerIs)
      throws Exception {
    MvcResult results = this.mockMvc.perform(post("/battlesummary/{id}/{locationId}",
        Id,locationId).param("winner",winnerIs.toString()))
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
  private EnergyData GetEnergyData(String locationId) throws Exception {

    MvcResult results = this.mockMvc.perform(post("/energystation/{id}/{locationId}",
        Id,locationId))
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
  private RewardsData GetChestRewards(String locationId) throws Exception {

    MvcResult results = this.mockMvc.perform(post("/chests/{id}/{locationId}",
        Id,locationId))
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
  private PlayerData GetPlayerData(String newName) throws Exception {
    MvcResult results = this.mockMvc.perform(get("/users/{id}",Id)).andDo(print()).
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
  private void CreatePlayerData(String newName) throws Exception {
    PlayerData newData = GameService.CreateNewUser();
    newData.setName(newName);

    String json = new ObjectMapper().writeValueAsString(newData);
    this.mockMvc.perform(post("/users/{id}",Id)
        .contentType("application/json").content(json)).andDo(print()).andExpect(status().isOk());
  }

  /**
   * Delete the player's data
   * @throws Exception
   */
  private void DeletePlayerData() throws Exception {
    this.mockMvc.perform(delete("/users/{id}",Id)).andDo(print()).andExpect(status()
        .isOk());
  }

  /**
   * Util function that returns a world data request.
   * @return
   */
  private WorldDataRequest CreateWorldDataRequest() {
    WorldDataRequest request = new WorldDataRequest();
    request.northeast = new PLLatLng(48.8583701, 2.2944813);
    request.southwest = new PLLatLng(48.8446743,2.2488194);
    return request;
  }

  // endregion
}
