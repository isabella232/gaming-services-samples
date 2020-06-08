package com.google.maps.gaming.zoinkies;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point in the application server.
 *
 */
@SpringBootApplication
public class ZoinkiesApplication {
  @Autowired
  Firestore firestore;

  private String PROJECTID = "musk-samples"; // TODO Removed

  public static void main(String[] args) {
    SpringApplication.run(ZoinkiesApplication.class, args);

    FirestoreOptions firestoreOptions =
        FirestoreOptions.getDefaultInstance().toBuilder()
            .setProjectId("musk-samples")
            .build();
    Firestore db = firestoreOptions.getService();
  }
}
