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
package com.google.maps.gaming.zoinkies.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.maps.gaming.zoinkies.ITEMS;
import com.google.maps.gaming.zoinkies.models.PlayerData;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * This class handles the interactions with Firestore to CRUD player stats and inventory.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PlayerService {

  @Autowired
  Firestore firestore;

  /**
   * If it doesn't exist, create one
   * Otherwise return the current user data in the response
   *
   * @param Id The User Id
   * @return A Player Data record
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public PlayerData getPlayerData(String Id) throws ExecutionException, InterruptedException {
    ApiFuture<DocumentSnapshot> documentSnapshotApiFuture =
        this.firestore.document("users/" + Id).get();
    PlayerData data = null;
    DocumentSnapshot document = documentSnapshotApiFuture.get();
    if (document.exists()) {
      data = document.toObject(PlayerData.class);
    }
    return data;
  }

  /**
   * Deletes the player's stats and inventory.
   * @param Id Device generated Id identifying the player.
   * @implNote This function does not remove the world collection associated to the player.
   */
  public void removeUserData(String Id) {
    CollectionReference users = this.firestore.collection("users");
    Iterable<DocumentReference> documentReferences = users.listDocuments();
    documentReferences.forEach(documentReference -> {
      String id = documentReference.getId();
      if (id.equals(Id)) {
        // Remove the player's data
        try {
          documentReference.delete().get();
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Creates or updates a player's stats and inventory.
   *
   * @param Id  The generated device id identifying the player
   * @param newData The data to be created or updated
   * @return
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public PlayerData updatePlayerData(String Id, PlayerData newData)
      throws ExecutionException, InterruptedException {
    ApiFuture<DocumentSnapshot> documentSnapshotApiFuture =
        this.firestore.document("users/" + Id).get();
    this.firestore.document("users/"+Id).set(newData).get();
    newData = getPlayerData(Id);
    return newData;
  }
}
