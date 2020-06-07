package com.google.maps.gaming.zoinkies;

import java.util.ArrayList;
import java.util.List;

/**
 * Types of reference data
 * - Location Objects
 * - Collectibles
 * - Weapons
 * - Armors
 * - Characters
 */
public class ReferenceData {

  private List<ReferenceItem> References;

  public ReferenceData() {
    References = new ArrayList<>();
  }

  public List<ReferenceItem> getReferences() {
    return References;
  }

  public void setReferences(List<ReferenceItem> references) {
    References = references;
  }

  public ReferenceItem getReferenceItem(String id) {
    if (id == null || id.isEmpty()) {
      return null;
    }
    for(ReferenceItem ri:getReferences()) {
      if (ri.getId().equals(id)) {
        return ri;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (ReferenceItem ri : References ) {
      sb.append(ri.toString());
      sb.append("\n");
    }
    return sb.toString();
  }
}
