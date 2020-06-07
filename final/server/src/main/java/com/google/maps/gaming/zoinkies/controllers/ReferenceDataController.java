package com.google.maps.gaming.zoinkies.controllers;

import com.google.maps.gaming.zoinkies.services.GameService;
import com.google.maps.gaming.zoinkies.models.ReferenceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class handles all endpoints for reference data
 */
@RestController
public class ReferenceDataController {

  @Autowired
  private GameService GameService;

  /**
   * Returns the references described under resources
   * @return
   */
  @GetMapping("/references")
  public ResponseEntity<ReferenceData> GetReferences() {

    ReferenceData data = GameService.getReferenceData();
    if (data == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.ok(data);
  }
}
