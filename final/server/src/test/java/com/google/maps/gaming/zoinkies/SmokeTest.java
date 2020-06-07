package com.google.maps.gaming.zoinkies;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

  @Autowired
  private PlayerDataController controller;

  @Test
  public void contexLoads() throws Exception {
    assertThat(controller).isNotNull();
  }
/*
  @Test
  public void ReferenceDataLoads() throws Exception {
    String res = GetSample();
    System.out.println(res);
    assertThat(res).isNotEmpty();
  }

  private String GetSample() throws JsonProcessingException{

    ReferenceItem ri = new ReferenceItem();
    ri.setId(GameConstants.CHEST);
    ri.setAttackScore(0);
    ri.setDefenseScore(0);
    ri.setDescription("A War Chest brought from Zoinkies outer space");
    ri.setName("War Chest");
    Duration respawnDuration = Duration.ofMinutes(5); // 5mins
    ri.setRespawnDuration(respawnDuration);

    ReferenceData rl = new ReferenceData();
    rl.getReferences().add(ri);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    return objectMapper.writeValueAsString(rl);
  }

 */
}