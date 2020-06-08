package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class PLFilter {
  private int max_location_count;
  private PLSpacingOptions spacing;
  private String[] included_types;

  public int getMax_location_count() {
    return max_location_count;
  }

  public void setMax_location_count(int max_location_count) {
    this.max_location_count = max_location_count;
  }

  public PLSpacingOptions getSpacing() {
    return spacing;
  }

  public void setIncluded_types(String[] included_types) {
    this.included_types = included_types;
  }

  public String[] getIncluded_types() {
    return included_types;
  }

  public void setSpacing(PLSpacingOptions spacing) {
    this.spacing = spacing;
  }
}
