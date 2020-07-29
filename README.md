# Real World Game with Maps Gaming Services

This repository contains the source code of a simple front-to-back third person mobile game that uses Google Maps Platform gaming services to create interesting gameplay.

This repo is meant to be used alongside the [Geocasts for Gaming]() video series available on the Google Maps Platform YouTube Channel.

The video tutorials can be accessed here:
- [Part 1: Game setup]() - client, server, and database setup
- [Part 2: Persistent gameplay with Maps]() - server side game logic
- [Part 3: Persistent gameplay with Maps]() - client-server integration
- [Part 4: (final)/ Setup for mobile]() - running locally or deploying to AppEngine

## Repo structure

This repository has been split into four folders, each associated to a specific tutorial to allow you to see how the game is implementated in phases.

1. **/part1** focuses on the client only. It integrates with Unity Maps Gaming SDK and runs without server.

1. **/part2** focuses on the server only. It integrates with Playable Locations API, and generates game locations specific to the gameplay. It also integrates with Cloud Firestore. The server provides an implementation of all game REST API endpoints that will be later queries by the client.
It also comes with a battery of tests that can be run locally.

1. **/part3** focuses on the client server integration, and more specifically on the handling of spawn locations, and their lifecycle.
Both client and server are still run locally at this point.

1. **/final** contains the full version of the game client and game server, with mobile + GPS support on the client side, ready for deployment on GCP App Engine for the server.
Note that the server java code is identical in part2, part3 and final. The only differences are with the build and deployment files.

## Architecture

The game has three major components that you will work with:

- A Unity game client written in C#: the game!
- A game server written in Java: serves the REST APIs that allow the client to communicate with [Cloud Firestore](https://firebase.google.com/docs/firestore/) and the Google Maps Platform [Playable Locations API](https://developers.google.com/maps/documentation/gaming/getting_started_locations). [Gradle](https://gradle.org/) is used as a build tool.
- A [Cloud Firestore](https://firebase.google.com/docs/firestore/) database: persist game state and data.

### The game server

The game server is written in Java and uses the following to do most of the heavy lifting:

- [Spring Boot framework](https://spring.io/guides/gs/spring-boot/) to stand up the server.
- [Gradle](https://gradle.org/install/) as a build configuration system.
- Spring-boot-starter-web for REST support.
- Spring-cloud-gcp-starter for the auto-configuration of GCP core components.
- Spring-cloud-gcp-starter-firestore for GCP Cloud Firestore support.
- Firestore for data persistence.

## Before you begin

### Prerequisites

The following is required to build and run this game:

- A Google Cloud Platform account.
- A GCP Project with Google Maps Platform gaming services enabled. [Click here to enable it for your project](https://console.cloud.google.com/google/maps-apis/start?products=Gaming).
- A GCP API key for the project with Google Maps Platform gaming services enabled. This key is used by the client and server.
- The [gcloud CLI](https://cloud.google.com/sdk/docs/quickstarts).
- Unity version 2018.4 or later. The Unity client code for this game has been tested on Unity 2019.2.

### Dependencies

The Unity client under Part 1, 3 and 4 relies on two libraries:

1. [Google Maps SDK for Unity](https://cloud.google.com/maps-platform/gaming)
2. [Unity LitJson](https://github.com/Mervill/UnityLitJson)

Before you begin, download these libraries. You will import them into your project in the Unity editor in the setup steps.

Note: All Unity projects will show compilation errors and missing scripts without the two libraries.

## Running the game server

To run the game server, you will do the following:

1. Set your API key.
1. Set up Firestore.
1. Deploy the server.

### Setting your API key

The server is used to proxy requests from the Unity client to the Playable Locations API.

For this to work, you need to provide your GCP API key in `PlayableLocationsService.java`:

```java
private final String API_KEY = "<YOUR API KEY HERE>";
```

### Setting up Firestore

The easiest way to use Firestore with this repo is to create a GCP service account that will allow the game server to access the datastore, and store its credentials in an environment variable that is accessible by the server.

To set up a Firestore database, do the following:

1. [Create a Firestore in Native mode](https://cloud.google.com/firestore/docs/quickstart-servers#create_a_in_native_mode_database)
1. [Create a GCP service account](https://cloud.google.com/iam/docs/creating-managing-service-accounts#iam-service-accounts-create-gcloud) and assign it the *cloud datastore user* role. This will allow the service account to read/write to the Firestore database.
1. [Create a JSON service account key](https://cloud.google.com/iam/docs/creating-managing-service-account-keys#creating_service_account_keys).
1. Create an environment variable called `GOOGLE_APPLICATION_CREDENTIALS` that is set to the location of the JSON key file. The GCP modules used in the project will automatically recognize the variable and connect to the gcp project described in the json file.

### Deploying the server

The game server is designed to run on either localhost or on AppEngine. For development purposes, running the server on localhost is quick and easy using the glcoud CLI. If you wish to run the finished game on a mobile device, you will need to deploy to AppEngine so that the server is publicly accessible.

#### Localhost

To run the server locally, do the following:

1. Login to the gcloud CLI
``` shell
gcloud auth login
```
1. Select your project
``` shell
gcloud config set project <your-project-id>
```
1. Run the server using Spring Boot
``` shell
./gradlew bootRun
```

### AppEngine

To deploy the game server to AppEngine, do the following:

1. Add your GCP Project ID to `build.gradle`:
``` shell
appengine {  // App Engine tasks configuration
    deploy {   // deploy configuration
        projectId = "YOUR PROJECT ID HERE"
        version = '1'
    }
}
1. Run the following commands from the CLI:
- Clean the build directory:
``` shell
./gradlew clean
```
- Build the Spring Boot jar file:
``` shell
./gradlew bootJar
```
- Deploy to AppEngine:
``` shell
./gradlew appengineDeploy
```

## Running the Unity client code

If you have not already, configure and run the game server. The Unity client relies on the availability of this server to read and write game data, as well as proxy to the Playable Locations API.

To run the client code in Unity, do the following:

1. Import the LitJson and Maps SDK for Unity dependencies into your project.
1. Set your API key in the `MapsService` component
1. Set the `SERVER_URL` to point to the game server.
1. Hit the play button to run the game!

### Importing the dependencies

To import the Unity LitJson and Maps SDK for Unity depdencies into your project, do the following in the Unity editor for each dependency:

1. Select *'Assets' > 'Import Package' > 'Custom Package...'*
1. Select the `.unitypackage` file for the dependency. The assets in the package are displayed.
1. Click the 'Import' button.

### Setting your API key

A valid GCP API key for your project is required for the Unity client to retrieve the data from Google Maps Platform gaming services for redendering the map as objects in Unity.

To set your API key, do the following in the Unity editor:

1. In the Project window, under `/Assets/Zoinkies/Scenes`, click `ZoinkiesMain`. The contents of the asset will appear in the Hierarchy window.
1. In the Hierarchy window, under `/GameWorld`, click `WorldContainer`. The container settings will appear in the Inspector window.
1. Set your Google Maps Platform API key in the 'Api Key' field of the `MapsService` component.

### Setting the server URL

Calls to the Playable Locations API and the Firestore database from the Unity client are proxied through the game server. To enable this, do the following:

1. In `/client/Zoinkies/Assets/Scripts/Services`, open `ServerManager.cs`
1. Set the appropriate target URL in the `SERVER_URL` constant.
- Localhost
``` shell
private const string SERVER_URL = "http://localhost:8080";
```
- AppEngine
``` shell
private const string SERVER_URL = "https://<YOUR PROJECT ID HERE>.wl.r.appspot.com";
```
