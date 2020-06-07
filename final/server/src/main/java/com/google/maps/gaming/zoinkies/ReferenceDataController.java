package com.google.maps.gaming.zoinkies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReferenceDataController {

  @Autowired
  private GameUtils GameService;

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
