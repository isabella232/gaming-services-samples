package com.google.maps.gaming.zoinkies;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PlayerService {

  @Autowired
  Firestore firestore;

  /**
   * If it doesn't exist, create one
   * Otherwise return the current user data in the response
   *
   * @param Id
   * @return
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public PlayerData GetPlayerData(String Id) throws ExecutionException, InterruptedException {

    ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = this.firestore.document("users/" + Id).get();

    PlayerData data = null;
    DocumentSnapshot document = documentSnapshotApiFuture.get();
    if (document.exists()) {
      data = document.toObject(PlayerData.class);
      System.out.println("Existing Document: " + document.getData());
    }
    System.out.println("read: " + data);
    return data;
  }

  /**
   * Deletes the player's stats and inventory.
   * @param Id Device generated Id identifying the player.
   * @implNote This function does not remove the world collection associated to the player.
   */
  public void RemoveUserData(String Id) {
    CollectionReference users = this.firestore.collection("users");
    Iterable<DocumentReference> documentReferences = users.listDocuments();
    documentReferences.forEach(documentReference -> {
      String id = documentReference.getId();
      System.out.println("user id=" + id);
      if (id.equals(Id)) {
        // Remove the player's data
        System.out.println("removing user id: " + id);
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
  public PlayerData UpdatePlayerData(String Id, PlayerData newData) throws ExecutionException, InterruptedException {

    ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = this.firestore.document("users/" + Id).get();
    WriteResult writeResult = this.firestore.document("users/"+Id).set(newData).get();

    System.out.println("Update time: " + writeResult.getUpdateTime());
    // Return updated record
    newData = GetPlayerData(Id);
    return newData;
  }
}
