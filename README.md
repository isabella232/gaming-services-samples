# Real World Game with Maps Gaming Services

This repository contains the source code of a simple front-to-back third person mobile game that uses the Unity Maps Gaming SDK and Playable Locations API to create interesting gameplay.
The code supports the information provided into a series of video tutorials accessible <HERE>.

The game client is implemented in C# and has been tested on Unity 2019.2.
The game server is implemented in Java based on Spring Boot and uses REST apis to communicate with the client.
The server connects to Cloud Firestore as a database, and the project uses gradle as a build tool.

The tutorials can be accessed here:
Part 1/ Game setup
  <PROVIDE URL>
Part 2/ Persistent gameplay with Maps - Server side game logic
Part 3/ Persistent gameplay with Maps - Client Server integration
Part 4 (final)/ Setup for mobile

## Pre-requisites

Both client and server call Maps Gaming Services and require appropriate API Keys, therefore a GCP project with access to the Semantic Tile API and Playable Locations API is needed to run the game demo.

## Project structure
The repository has been split into 4 different folders, each of these associated to a specific tutorial, and giving a sense of progression with the implementation.

1. Part1 focuses on the client only. It integrates with Unity Maps Gaming SDK and runs without server.

1. Part2 focuses on the server only. It integrates with Playable Locations API, and generates game locations specific to the gameplay. It also integrates with Cloud Firestore. The server provides an implementation of all game REST API endpoints that will be later queries by the client.
It also comes with a battery of tests that can be run locally.

1. Part3 focuses on the client server integration, and more specifically on the handling of spawn locations, and their lifecycle.
Both client and server are still run locally at this point.

1. The final folder contains the full version of the game client and game server, with mobile + GPS support on the client side, and ready for deployment on GCP App Engine for the server.
Note that the server java code is identical in part2, part3 and final. The only differences are with the build and deployment files.

## Running the client code

The client code requires an API Key to run.
The Key should be set on the MapsService component attached to the WorldContainer gameobject parented to GameWorld object in the main scene.


## Building and running the server code locally
The server requires an API Key to query Playable Locations API. This API Key is set in the PlayableLocationsService class.

Under the server root folder of modules 2, 3 or final:

``` shell
./gradlew bootRun
```

## Building and running the server remotely
1. In the final module under the server folder:
In addition of the API Key for playable locations, the GCP Project Id must be added to the build.gradle file:

``` shell
appengine {  // App Engine tasks configuration
    deploy {   // deploy configuration
        projectId = "YOUR PROJECT ID HERE"
        version = '1'
    }
}
```

1. You must be authenticated locally to access the services in the GCP Project.
This can be done by following the instructions on this page:
https://cloud.google.com/source-repositories/docs/authentication#authenticate-using-the-cloud-sdk

1. Run the following commands from the CLI:
``` shell
./gradlew clean
./gradlew bootJar
./gradlew appengineDeploy
```

1. Make sure that the client points to your public SERVER URL
In the final module under the client folder:
Locate and update the ServerManager.cs file.
Update the SERVER URL to point to your URL.

``` shell
public class ServerManager : MonoBehaviour {
  //private const string SERVER_URL = "http://localhost:8080";
  private const string SERVER_URL = "https://<YOUR PROJECT ID HERE>.wl.r.appspot.com";
```
