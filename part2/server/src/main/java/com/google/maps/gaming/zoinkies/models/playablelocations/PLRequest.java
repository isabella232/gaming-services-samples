package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class PLRequest {
  private PLAreaFilter areaFilter;
  private PLCriteria[] criteria;

  public PLAreaFilter getAreaFilter() {
    return areaFilter;
  }

  public void setAreaFilter(
      PLAreaFilter areaFilter) {
    this.areaFilter = areaFilter;
  }

  public PLCriteria[] getCriteria() {
    return criteria;
  }

  public void setCriteria(PLCriteria[] criteria) {
    this.criteria = criteria;
  }
}
