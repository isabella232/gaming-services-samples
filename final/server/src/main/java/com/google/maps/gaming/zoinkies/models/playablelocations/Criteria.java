package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class Criteria {
  /**
   * The game object type
   */
  private int game_object_type;

  /**
   * Getter for game_object_type
   * @return the game object type as an int
   */
  public int getGame_object_type() {
    return game_object_type;
  }

  /**
   * Setter for game_object_type
   * @param game_object_type
   */
  public void setGame_object_type(int game_object_type) {
    this.game_object_type = game_object_type;
  }

  /**
   * The filter used by the playable locations query
   */
  private Filter filter;

  /**
   * Getter for filter
   * @return
   */
  public Filter getFilter() {
    return filter;
  }

  /**
   * Setter for filter
   * @param filter
   */
  public void setFilter(Filter filter) {
    this.filter = filter;
  }

  /**
   * The fields to return in the query to playable locations
   */
  private FieldMask fields_to_return;

  /**
   * Getter for fields_to_return
   * @return
   */
  public FieldMask getFields_to_return() {
    return fields_to_return;
  }

  /**
   * Setter for fields_to_return
   * @param fields_to_return
   */
  public void setFields_to_return(
      FieldMask fields_to_return) {
    this.fields_to_return = fields_to_return;
  }
}
