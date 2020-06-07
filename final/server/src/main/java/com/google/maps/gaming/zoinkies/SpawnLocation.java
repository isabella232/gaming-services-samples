package com.google.maps.gaming.zoinkies;

import com.google.maps.gaming.zoinkies.playablelocations.PLLatLng;
import jdk.jfr.Timestamp;

public class SpawnLocation {
  public String id;
  public Boolean active;
  public String object_type_id;
  public Boolean respawns;
  public String respawn_time;
  public int number_of_keys_to_activate;
  public String key_type_id;
  public PLLatLng snappedPoint;

  public SpawnLocation() {}

  public SpawnLocation(String id,
      Boolean active,
      String object_type_id,
      Boolean respawns,
      String respawn_time,
      int number_of_keys_to_activate,
      String key_type_id,
      PLLatLng snappedPoint) {
    this.id = id;
    this.active = active;
    this.object_type_id = object_type_id;
    this.respawns = respawns;
    this.key_type_id = key_type_id;
    this.respawn_time = respawn_time;
    this.number_of_keys_to_activate = number_of_keys_to_activate;
    this.snappedPoint = snappedPoint;
  }
}
