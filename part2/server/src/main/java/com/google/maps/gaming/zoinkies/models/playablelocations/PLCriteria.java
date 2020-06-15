package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class PLCriteria {

  private int game_object_type;
  private PLFilter filter;
  private PLFieldMask fields_to_return;

  public int getGame_object_type() {
    return game_object_type;
  }

  public void setGame_object_type(int game_object_type) {
    this.game_object_type = game_object_type;
  }

  public PLFilter getFilter() {
    return filter;
  }

  public void setFilter(PLFilter filter) {
    this.filter = filter;
  }

  public PLFieldMask getFields_to_return() {
    return fields_to_return;
  }

  public void setFields_to_return(
      PLFieldMask fields_to_return) {
    this.fields_to_return = fields_to_return;
  }
}
