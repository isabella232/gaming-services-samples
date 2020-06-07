package com.google.maps.gaming.zoinkies;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.maps.gaming.zoinkies.controllers.PlayerDataController;
import com.google.maps.gaming.zoinkies.controllers.ReferenceDataController;
import com.google.maps.gaming.zoinkies.controllers.WorldDataController;
import com.google.maps.gaming.zoinkies.services.GameService;
import com.google.maps.gaming.zoinkies.services.PlayableLocationsService;
import com.google.maps.gaming.zoinkies.services.PlayerService;
import com.google.maps.gaming.zoinkies.services.WorldService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureMockMvc
@SpringBootTest
class ZoinkiesApplicationTests {

  @Autowired
  private PlayerDataController playerDataController;
  @Autowired
  private WorldDataController worldDataController;
  @Autowired
  private ReferenceDataController referenceDataController;
  @Autowired
  private PlayerService playerService;
  @Autowired
  private WorldService worldService;
  @Autowired
  private GameService gameService;
  @Autowired
  private PlayableLocationsService playableLocationsService;

  @Test
  public void contexLoads() throws Exception {
    assertThat(playerDataController).isNotNull();
    assertThat(worldDataController).isNotNull();
    assertThat(referenceDataController).isNotNull();
    assertThat(playerService).isNotNull();
    assertThat(worldService).isNotNull();
    assertThat(gameService).isNotNull();
    assertThat(playableLocationsService).isNotNull();
  }

}
