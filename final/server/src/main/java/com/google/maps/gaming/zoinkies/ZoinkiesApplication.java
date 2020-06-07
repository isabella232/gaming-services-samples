package com.google.maps.gaming.zoinkies;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class ZoinkiesApplication {
  @Autowired
  Firestore firestore;

  public static void main(String[] args) {
    SpringApplication.run(ZoinkiesApplication.class, args);

    /*
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.scan("com.google.maps.gaming.zoinkies");
    context.refresh();*/

    FirestoreOptions firestoreOptions =
        FirestoreOptions.getDefaultInstance().toBuilder()
            .setProjectId("musk-samples")
            .build();
    Firestore db = firestoreOptions.getService();
  }
}
