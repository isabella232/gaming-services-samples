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
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using Google.Maps.Coord;
using Google.Maps.Examples;
using Google.Maps.Examples.Shared;
using Google.Maps.Feature.Style;
using Unity.Collections;
using UnityEngine;
using UnityEngine.Assertions;
using UnityEngine.Events;
using Random = UnityEngine.Random;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    ///     String event class used to pass string parameters to other Unity gameobjects.
    /// </summary>
    [Serializable]
    public class StringEvent : UnityEvent<string>
    {
    }

    /// <summary>
    ///     This class initializes reference, player, map data.
    ///     It also manages the creation and maintenance of spawned game objects.
    /// </summary>
    public class WorldController : BaseMapLoader
    {
        /// <summary>
        ///     Dispatched to the game when Reference Data, World Data and Player Data
        ///     are initialized.
        /// </summary>
        public UnityEvent GameReady;

        /// <summary>
        ///     Dispatched to the game when some loading errors occur.
        /// </summary>
        public StringEvent GameLoadingError;

        /// <summary>
        ///     Reference to the main camera
        /// </summary>
        public Camera mainCamera;

        /// <summary>
        ///     Reference to the battle camera
        /// </summary>
        public Camera battleCamera;

        /// <summary>
        ///     Reference to the server Manager, responsible for all REST calls.
        /// </summary>
        public ServerManager ServerManager;

        /// <summary>
        ///     Convenient container to hold all spawned locations for quick access.
        /// </summary>
        public Transform PlayableLocationsContainer;

        /// <summary>
        ///     Tower prefab
        /// </summary>
        public GameObject TowerPrefab;

        /// <summary>
        ///     Minion prefab
        /// </summary>
        public GameObject MinionPrefab;

        /// <summary>
        ///     Chest prefab
        /// </summary>
        public GameObject ChestPrefab;

        /// <summary>
        ///     Energy station prefab
        /// </summary>
        public GameObject EnergyStationPrefab;

        /// <summary>
        ///     Reference to avatar
        /// </summary>
        public GameObject Avatar;

        /// <summary>
        ///     Reference to ground material
        /// </summary>
        public Material GroundMaterial;

        /// <summary>
        ///     Reference to roads material
        /// </summary>
        public Material RoadsMaterial;

        /// <summary>
        ///     Reference to list of building walls materials
        /// </summary>
        public List<Material> BuildingsWallMaterials;

        /// <summary>
        ///     Reference to list of building roof materials
        /// </summary>
        public List<Material> BuildingsRoofMaterials;

        /// <summary>
        ///     Reference to material for modeled structures
        /// </summary>
        public Material ModeledBuildingsMaterial;

        /// <summary>
        ///     Distance inside which buildings will be completely squashed (<see cref="MaximumSquash" />)
        /// </summary>
        public float SquashNear = 50;

        /// <summary>
        ///     Distance outside which buildings will not be squashed.
        /// </summary>
        public float SquashFar = 200;

        /// <summary>
        ///     The vertical scaling factor applied at maximum squashing.
        /// </summary>
        public float MaximumSquash = 0.1f;

        /// <summary>
        ///     Indicates if we have acquired a GPS Location
        /// </summary>
        [ReadOnly]
        public bool HasGPSLocation;

        /// <summary>
        ///     Setup milestone: Playable Locations loaded and initialized.
        /// </summary>
        private const string PLAYABLE_LOCATIONS_INITIALIZED =
            "PLAYABLE_LOCATIONS_INITIALIZED";

        /// <summary>
        ///     Setup milestone: Reference data loaded and initialized.
        /// </summary>
        private const string REFERENCE_DATA_INITIALIZED = "REFERENCE_DATA_INITIALIZED";

        /// <summary>
        ///     Setup milestone: Player data loaded and initialized.
        /// </summary>
        private const string PLAYER_DATA_INITIALIZED = "PLAYER_DATA_INITIALIZED";

        /// <summary>
        ///     Setup milestone: Map data loaded and initialized.
        /// </summary>
        private const string MAP_INITIALIZED = "MAP_INITIALIZED";

        /// <summary>
        ///     Frequency at which we check our device location (to save battery).
        /// </summary>
        private const float LOCATION_PING = 10f; // 10 seconds

        /// <summary>
        ///     Indicates if the floating origin has been set
        /// </summary>
        private bool _floatingOriginIsSet;

        /// <summary>
        ///     Keeps track of game objects already instantiated.
        ///     We only display game objects within a certain range.
        /// </summary>
        private Dictionary<string, GameObject> _spawnedGameObjects;

        /// <summary>
        ///     Reference to the styles options applied to loaded map features
        /// </summary>
        private GameObjectOptions _zoinkiesStylesOptions;

        /// <summary>
        ///     Keeps track of startup milestones
        /// </summary>
        private List<string> _startupCheckList;

        /// <summary>
        ///     Indicates if the game has started
        /// </summary>
        private bool _gameStarted;

        /// <summary>
        ///     Indicates if we have a pending request to get world data.
        ///     Used for optimization.
        /// </summary>
        private bool _worldDataIsLoading;

        /// <summary>
        ///     Current value of ping location timer.
        /// </summary>
        private float _currentTimer;

        /// <summary>
        ///     Sets up the setup milestones list.
        ///     Loads reference data and player data.
        /// </summary>
        void Awake()
        {
            // Initialize start up check list
            _startupCheckList = new List<string>
            {
                PLAYABLE_LOCATIONS_INITIALIZED,
                REFERENCE_DATA_INITIALIZED,
                PLAYER_DATA_INITIALIZED,
                MAP_INITIALIZED
            };

            // Load and initialize Reference Data
            StartCoroutine(ServerManager.GetReferenceData(data =>
            {
                ReferenceService.GetInstance().Init(data);
                _startupCheckList.Remove(REFERENCE_DATA_INITIALIZED);
                CheckStartConditions();
            }, OnError));

            // Load and initialize Player Data
            StartCoroutine(ServerManager.GetPlayerData(data =>
            {
                PlayerService.GetInstance().Init(data);
                _startupCheckList.Remove(PLAYER_DATA_INITIALIZED);
                CheckStartConditions();
            }, OnError));

            LoadOnStart = false;
        }

        /// <summary>
        ///     Performs the initial Map load
        /// </summary>
        protected override void Start()
        {
            Assert.IsNotNull(mainCamera);
            Assert.IsNotNull(battleCamera);
            Assert.IsNotNull(PlayableLocationsContainer);
            Assert.IsNotNull(ServerManager);
            Assert.IsNotNull(Avatar);
            base.Start();

            _spawnedGameObjects = new Dictionary<string, GameObject>();

            if (BuildingsRoofMaterials.Count == 0
                || BuildingsWallMaterials.Count == 0
                || BuildingsRoofMaterials.Count != BuildingsWallMaterials.Count)
            {
                throw new System.Exception("We expect at least one wall " +
                                           "and one roof material.");
            }
        }

        void Update()
        {
            if (_gameStarted)
            {
                _currentTimer += Time.deltaTime;
                if (Input.location.status == LocationServiceStatus.Running &&
                    _currentTimer > LOCATION_PING)
                {
                    LocationInfo locInfo = Input.location.lastData;
                    // Compare distance from current location to floating origin
                    // If distance is greater than 500 meters,
                    // let's reinitialize the floating origin.
                    Vector3 locInfoPos =
                        MapsService.Coords.FromLatLngToVector3(new LatLng(locInfo.latitude,
                            locInfo.longitude));
                    float dist = Vector3.Distance(locInfoPos, Vector3.zero);
                    if (dist > 1000f)
                    {
                        OnLocationServicesEvalComplete(locInfo);
                    }
                    _currentTimer = 0f;
                }
            }
        }

        /// <summary>
        ///     This function performs a sync of important data with the server.
        ///     Althought the client is making local game changes for usability reasons
        ///     the server is the ultimate authority for player stats and inventory
        ///     as well as spawn locations states.
        /// </summary>
        public void SyncData()
        {
            if (PlayerService.GetInstance().DataIsUntrusted)
            {
                StartCoroutine(ServerManager.PostPlayerData(PlayerService.GetInstance().Data,
                    data => { PlayerService.GetInstance().Init(data); }, OnError));
            }
        }

        /// <summary>
        ///     Triggered when the map is being reloaded.
        ///     For first time invocations, we request the GPS location if enabled. Otherwise this call
        ///     is just delegating the map reload to the base class.
        /// </summary>
        public override void LoadMap()
        {
            // LoadMap is called from Dynamic updater
            // Get our new GPS coordinates and use these to load the map
            // We don't need to update the floating origin all the time.

            if (_gameStarted)
            {
                base.LoadMap();
            }
            else
            {
                StartCoroutine(GetGPSLocation(OnLocationServicesEvalComplete));
            }
        }

        /// <summary>
        ///     Event listener triggered by the game when transitioning from Battle to World Mode.
        ///     References to this method are implicit.
        /// </summary>
        public void OnShowWorld()
        {
            Avatar.gameObject.SetActive(true);
            GetComponent<DynamicMapsUpdater>().enabled = true;

            mainCamera.enabled = true;
            battleCamera.enabled = false;

            // Good time to do a server sync
            SyncData();
        }

        /// <summary>
        ///     Event listener triggered by the game when transitioning from World to Battle Mode.
        ///     References to this method are implicit.
        /// </summary>
        public void OnShowBattleground()
        {
            Avatar.gameObject.SetActive(false);
            GetComponent<DynamicMapsUpdater>().enabled = false;

            mainCamera.enabled = false;
            battleCamera.enabled = true;
        }

        /// <summary>
        ///     Triggered when the map load starts.
        /// </summary>
        public void OnMapLoadStart()
        {
            // We've moved enough to restart a new map

            // Get all game objects near our avatar (us)
            // Note that we position the camera at the current GPS coordinates,
            // so the Avatar position is slightly offset.
            // At start, the Avatar is positioned at the origin of the world space,
            // which coincide with the floating origin lat lng.
            LatLng avatarLatLng = MapsService.Coords.FromVector3ToLatLng(Avatar.transform.position);
            UpdateWorldData(avatarLatLng, MaxDistance);
        }

        /// <summary>
        ///     Triggered by the UI when a new game needs to be created.
        ///     This event listener resets player and world data.
        /// </summary>
        public void OnNewGame()
        {
            _spawnedGameObjects.Clear();

            // Clear the world
            ClearContainer(PlayableLocationsContainer);

            _gameStarted = false;

            PlayerService.GetInstance().Init(new PlayerData());
            WorldService.GetInstance().Init(new WorldData());

            // Maps data stay the same (we are at the same location)PLAYER_DATA_INITIALIZED,
            _startupCheckList = new List<string> {PLAYABLE_LOCATIONS_INITIALIZED};

            // Reset Player data

            StartCoroutine(ServerManager.PostPlayerData(null, data =>
            {
                PlayerService.GetInstance().Init(data);
                _startupCheckList.Remove(PLAYER_DATA_INITIALIZED);
                CheckStartConditions();
            }, s => { Debug.LogError(s); }));


            StartCoroutine(ServerManager.DeleteWorldData(data =>
            {
                // Generate new world
                UpdateWorldData(LatLng, MaxDistance);
            }, s => { Debug.LogError(s); }));
        }

        /// <summary>
        ///     Sets the lat lng to our current position is the GPS is enabled
        ///     Otherwise use the default position, which is currently the Googleplex
        ///     in Mountain View, CA
        /// </summary>
        /// <param name="onInitComplete">
        ///     The callback triggered when we have a valid location info
        /// </param>
        /// <returns>An enumerator for a coroutine</returns>
        protected IEnumerator GetGPSLocation(Action<LocationInfo> onInitComplete)
        {
            Assert.IsNotNull(onInitComplete);

            // Invalidate the previous position
            HasGPSLocation = false;
            LocationInfo locInfo = new LocationInfo();

            if (Input.location.isEnabledByUser)
            {
                // Start service before querying location
                Input.location.Start();

                // Wait until service initializes
                int maxWait = 20;
                while (Input.location.status == LocationServiceStatus.Initializing && maxWait > 0)
                {
                    yield return new WaitForSeconds(1);
                    maxWait--;
                }

                // Service didn't initialize in 20 seconds
                if (maxWait < 1)
                {
                    onInitComplete.Invoke(locInfo);
                    yield break;
                }

                // Connection has failed
                if (Input.location.status == LocationServiceStatus.Failed)
                {
                    // Failed to get
                    onInitComplete.Invoke(locInfo);
                    yield break;
                }

                locInfo = Input.location.lastData;
                HasGPSLocation = true;

                // Success - locInfo is set
                onInitComplete.Invoke(locInfo);
            }
            else
            {
                onInitComplete.Invoke(locInfo);
            }
        }

        /// <summary>
        ///     Initializes the style options for this game, by setting materials to roads,
        ///     buildings and water areas.
        /// </summary>
        protected override void InitStylingOptions()
        {
            _zoinkiesStylesOptions = ExampleDefaults.DefaultGameObjectOptions;

            // The default maps shader has a glossy property that allows the sky to reflect on it.
            Material waterMaterial =
                ExampleDefaults.DefaultGameObjectOptions.RegionStyle.FillMaterial;
            waterMaterial.color = new Color(0.4274509804f, 0.7725490196f, 0.8941176471f);

            _zoinkiesStylesOptions.ModeledStructureStyle = new ModeledStructureStyle.Builder
            {
                Material = ModeledBuildingsMaterial
            }.Build();

            _zoinkiesStylesOptions.RegionStyle = new RegionStyle.Builder
            {
                FillMaterial = GroundMaterial
            }.Build();

            _zoinkiesStylesOptions.AreaWaterStyle = new AreaWaterStyle.Builder
            {
                FillMaterial = waterMaterial
            }.Build();

            _zoinkiesStylesOptions.LineWaterStyle = new LineWaterStyle.Builder
            {
                Material = waterMaterial
            }.Build();

            _zoinkiesStylesOptions.SegmentStyle = new SegmentStyle.Builder
            {
                Material = RoadsMaterial
            }.Build();

            if (RenderingStyles == null)
            {
                RenderingStyles = _zoinkiesStylesOptions;
            }
        }

        /// <summary>
        ///     Adds some squashing behavior to all extruded structures.
        ///     Basically, we squash everything around our Avatar so that generated game items
        ///     can be seen from a distance.
        /// </summary>
        protected override void InitEventListeners()
        {
            base.InitEventListeners();

            if (MapsService == null)
            {
                return;
            }

            // Apply a pre-creation listener that picks a random style for extruded buildings
            MapsService.Events.ExtrudedStructureEvents.WillCreate.AddListener(
                e =>
                {
                    int i = Random.Range(0, BuildingsRoofMaterials.Count);
                    e.Style = new ExtrudedStructureStyle.Builder
                    {
                        RoofMaterial = BuildingsRoofMaterials[i],
                        WallMaterial = BuildingsWallMaterials[i]
                    }.Build();
                });

            // Apply a pre-creation listener that picks a random style for modeled buildings
            // In this game, modeled buildings are plain and unicolor.
            MapsService.Events.ModeledStructureEvents.WillCreate.AddListener(
                e =>
                {
                    int i = Random.Range(0, BuildingsRoofMaterials.Count);
                    e.Style = new ModeledStructureStyle.Builder
                    {
                        Material = BuildingsRoofMaterials[i]
                    }.Build();
                });

            // Apply a post-creation listener that adds the squashing MonoBehaviour
            // to each building.
            MapsService.Events.ExtrudedStructureEvents.DidCreate.AddListener(
                e => { AddSquasher(e.GameObject); });

            // Apply a post-creation listener that adds the squashing MonoBehaviour
            // to each building.
            MapsService.Events.ModeledStructureEvents.DidCreate.AddListener(
                e => { AddSquasher(e.GameObject); });

            // Apply a post-creation listener that move road segments up to prevent  .
            MapsService.Events.SegmentEvents.DidCreate.AddListener(
                e =>
                {
                    // Move y position up to prevent z-fighting with water areas;
                    e.GameObject.transform.position += new Vector3(0f, 0.01f, 0f);
                });

            MapsService.Events.MapEvents.Loaded.AddListener(arg0 =>
            {
                _startupCheckList.Remove(MAP_INITIALIZED);
                CheckStartConditions();
            });
        }

        /// <summary>
        ///     Loads all playable locations within a round area centered on the player's
        ///     GPS position.
        /// </summary>
        /// <param name="currentPosition">The current lat lng of our avatar</param>
        /// <param name="distance">The radius of the map area around us</param>
        private void UpdateWorldData(LatLng currentPosition, float distance)
        {
            if (!_worldDataIsLoading)
            {
                _worldDataIsLoading = true;

                Vector3 center = MapsService.Coords.FromLatLngToVector3(currentPosition);

                Vector3 NorthEastCorner = center + new Vector3(distance, 0f, distance);
                LatLng NorthEastLatLng = MapsService.Coords.FromVector3ToLatLng(NorthEastCorner);

                Vector3 SouthWestCorner = center + new Vector3(-distance, 0f, -distance);
                LatLng SouthWestLatLng = MapsService.Coords.FromVector3ToLatLng(SouthWestCorner);

                WorldDataRequest wdr = new WorldDataRequest();
                wdr.CopyFrom(SouthWestLatLng, NorthEastLatLng);

                StartCoroutine(ServerManager.PostWorldData(wdr, OnWorldDataLoaded, OnError));
            }
        }

        /// <summary>
        ///     Creates all new spawn locations for the game from the World Data provided
        ///     by the server.
        /// </summary>
        /// <param name="worldData">World Data</param>
        private void CreateNewLocations(WorldData worldData)
        {
            // Render the new data on the map:
            // Only render the playable locations that are within range of the loaded map.
            // The API returns all locations within the overlapping cells - which may be way larger
            // than our map.
            CreateAssets(WorldService.GetInstance().GetMinions(),
                MinionPrefab,
                PlayableLocationsContainer);
            CreateAssets(WorldService.GetInstance().GetTowers(), TowerPrefab,
                PlayableLocationsContainer);
            CreateAssets(WorldService.GetInstance().GetChests(), ChestPrefab,
                PlayableLocationsContainer);
            CreateAssets(WorldService.GetInstance().GetEnergyStations(),
                EnergyStationPrefab,
                PlayableLocationsContainer);
        }

        /// <summary>
        ///     This helper class creates all gameobjects based on the given collection
        ///     of spawn locations.
        /// </summary>
        /// <param name="collection">A collection of spawn locations</param>
        /// <param name="prefab">The prefab to instantiate for each location</param>
        /// <param name="container">The container that holds all created gameobjects</param>
        /// <returns>The amount of created assets</returns>
        private int CreateAssets(
            IEnumerable<SpawnLocation> collection,
            GameObject prefab,
            Transform container)
        {
            int numberOfObjectsCreated = 0;
            foreach (SpawnLocation loc in collection)
            {
                if (loc.snappedPoint != null)
                {
                    Vector3 pos = MapsService.Coords.FromLatLngToVector3(
                        new LatLng(loc.snappedPoint.latitude, loc.snappedPoint.longitude));
                    // Do we already have this object in our scene?
                    if (!_spawnedGameObjects.ContainsKey(loc.locationId))
                    {
                        GameObject go =
                            Instantiate(prefab, container);
                        go.transform.position = pos;

                        // The reference to placeId allows us to find the associated data
                        // through WorldService
                        go.name = loc.locationId;

                        BaseSpawnLocationController sl =
                            go.GetComponent<BaseSpawnLocationController>();
                        Assert.IsNotNull(sl);
                        sl.Init(loc.locationId);

                        _spawnedGameObjects.Add(loc.locationId, go);
                        numberOfObjectsCreated++;
                    }
                }
            }

            return numberOfObjectsCreated;
        }

        /// <summary>
        ///     Checks if start conditions are met. Dispatches a game ready event if they are.
        /// </summary>
        private void CheckStartConditions()
        {
            if (_startupCheckList.Count == 0 && !_gameStarted)
            {
                GameReady?.Invoke();
                _gameStarted = true;
            }
        }

        /// <summary>
        /// Triggered when a valid location info is returned from the Unity Input locations service.
        /// </summary>
        /// <param name="locationInfo">Location Info</param>
        private void OnLocationServicesEvalComplete(LocationInfo locationInfo)
        {
            if (HasGPSLocation) // aka we were able to get a valid GPS location
            {
                LatLng = new LatLng(locationInfo.latitude, locationInfo.longitude);
                MapsService.MoveFloatingOrigin(LatLng);
                Avatar.transform.position = MapsService.Coords.FromLatLngToVector3(LatLng);
            }

            base.LoadMap();
        }

        /// <summary>
        /// Triggered when the application has focus (on Mobile).
        /// When this happens, we enable/disable location services and eventually request
        /// an adjustment to our loaded map.
        /// </summary>
        /// <param name="hasFocus">Indicates if the app has focus.</param>
        private void OnApplicationFocus(bool hasFocus)
        {
            if (!_gameStarted)
            {
                return;
            }

            if (hasFocus)
            {
                StartCoroutine(GetGPSLocation(OnLocationServicesEvalComplete));
            }
            else if (Input.location.status == LocationServiceStatus.Running)
            {
                Input.location.Stop();
            }
        }

        /// <summary>
        /// Triggered when world data is loaded successfully.
        /// Note that by world data we imply all game specific locations as opposed to maps data
        /// loaded through the Maps Unity SDK.
        /// </summary>
        /// <param name="worldData">World Data</param>
        private void OnWorldDataLoaded(WorldData worldData)
        {
            // Init the playerservice with the new batch of data
            WorldService.GetInstance().Init(worldData);

            // Init gameobjects on the map
            IEnumerable<string> keysFromWorldData =
                WorldService.GetInstance().GetSpawnLocationsIds();

            // Delete all gameobjects not on the new list
            int deletedEntries = 0;
            List<string> missingKeysFromLocalCache =
                new List<string>(_spawnedGameObjects.Keys.Except(keysFromWorldData));
            foreach (string k in missingKeysFromLocalCache)
            {
                Destroy(_spawnedGameObjects[k].gameObject);
                _spawnedGameObjects.Remove(k);
                deletedEntries++;
            }

            // Add all new locations
            CreateNewLocations(worldData);

            // Show/hide objects based on distance from avatar
            foreach (string k in _spawnedGameObjects.Keys)
            {
                SpawnLocation sl = WorldService.GetInstance().GetSpawnLocation(k);
                Vector3 pos = MapsService.Coords.FromLatLngToVector3(
                    new LatLng(sl.snappedPoint.latitude, sl.snappedPoint.longitude));

                _spawnedGameObjects[k].SetActive(Vector3.Distance(Avatar.transform.position, pos) <=
                                                MaxDistance);
            }

            _startupCheckList.Remove(PLAYABLE_LOCATIONS_INITIALIZED);
            CheckStartConditions();

            _worldDataIsLoading = false;
        }

        /// <summary>
        /// Triggered when any server call fails with errors.
        /// </summary>
        /// <param name="errorMessage">An error message</param>
        private void OnError(string errorMessage)
        {
            Debug.LogError(errorMessage);
            if (_worldDataIsLoading)
            {
                _worldDataIsLoading = false;
            }

            // Dispatches an event to the game. This eventually bubbles up to the user interface.
            GameLoadingError?.Invoke(errorMessage);
        }

        /// <summary>
        ///     Clears all children of a transform
        /// </summary>
        /// <param name="container">A gameobject container</param>
        private void ClearContainer(Transform container)
        {
            foreach (Transform child in container)
            {
                Destroy(child.gameObject);
            }
        }

        /// <summary>
        ///     Adds a Squasher MonoBehaviour to the supplied GameObject.
        /// </summary>
        /// <remarks>
        ///     The Squasher MonoBehaviour reduced the vertical scale of the GameObject's transform
        ///     when a building object is nearby.
        /// </remarks>
        /// <param name="target">The GameObject to which to add the Squasher behaviour.</param>
        private void AddSquasher(GameObject target)
        {
            Squasher squasher = target.AddComponent<Squasher>();
            squasher.Target = Avatar.transform;
            squasher.Near = SquashNear;
            squasher.Far = SquashFar;
            squasher.MaximumSquashing = MaximumSquash;
        }
    }
}
