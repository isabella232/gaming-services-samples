package com.google.maps.gaming.zoinkies.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.Duration;

/**
 * A POJO class used to track reference items as loaded in the reference data list.
 */
public class ReferenceItem {

  /**
   * Unique identifier for this object
   */
  private String Id;
  /**
   * Friendly name
   */
  private String Name;
  /**
   * Object type
   */
  private String Type;
  /**
   * Friendly description
   */
  private String Description;
  /**
   * Base attack score
   */
  private int AttackScore;
  /**
   * Base defense score
   */
  private int DefenseScore;
  /**
   * Base attack cooldown
   */
  private Duration Cooldown;
  /**
   * Respawn duration. No value indicates the object does not respawn.
   */
  private Duration RespawnDuration;
  /**
   * Prefab name to be instantiated by the Unity client for this object.
   */
  private String Prefab;

  public String getId() {
    return Id;
  }

  public void setId(String id) {
    Id = id;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public String getType() {
    return Type;
  }

  public void setType(String type) {
    Type = type;
  }

  public String getDescription() {
    return Description;
  }

  public void setDescription(String description) {
    Description = description;
  }

  public int getAttackScore() {
    return AttackScore;
  }

  public void setAttackScore(int attackScore) {
    AttackScore = attackScore;
  }

  public int getDefenseScore() {
    return DefenseScore;
  }

  public void setDefenseScore(int defenseScore) {
    DefenseScore = defenseScore;
  }

  @JsonFormat(shape = Shape.STRING)
  public Duration getCooldown() {
    return Cooldown;
  }

  public void setCooldown(Duration cooldown) {
    Cooldown = cooldown;
  }

  @JsonFormat(shape = Shape.STRING)
  public Duration getRespawnDuration() {
    return RespawnDuration;
  }

  public void setRespawnDuration(Duration respawnDuration) {
    RespawnDuration = respawnDuration;
  }

  public String getPrefab() {
    return Prefab;
  }

  public void setPrefab(String prefab) {
    Prefab = prefab;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Id: " + Id);
    sb.append("Name: " + Name);
    sb.append("Type: " + Type);
    sb.append("Description: " + Description);
    sb.append("AttackScore: " + AttackScore);
    sb.append("DefenseScore: " + DefenseScore);
    sb.append("Cooldown: " + Cooldown);
    sb.append("RespawnDuration: " + RespawnDuration);
    sb.append("Prefab: " + Prefab);
    return sb.toString();
  }
}
