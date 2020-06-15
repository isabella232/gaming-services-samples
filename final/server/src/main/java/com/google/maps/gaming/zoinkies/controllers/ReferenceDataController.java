/**
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.maps.gaming.zoinkies.controllers;

import com.google.maps.gaming.zoinkies.services.GameService;
import com.google.maps.gaming.zoinkies.models.ReferenceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class handles all endpoints for reference data.
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
