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
using Google.Maps;
using Google.Maps.Coord;
using Google.Maps.Examples;
using Google.Maps.Examples.Shared;
using Google.Maps.Feature.Style;
using Unity.Collections;
using UnityEngine;
using UnityEngine.Assertions;
using UnityEngine.Events;

namespace Google.Maps.Demos.Zoinkies {

  /// <summary>
  /// This class initializes reference, player, map data.
  /// It also manages the creation and maintenance of spawned game objects.
  ///
  /// </summary>
  public class WorldController : BaseMapLoader {

    #region properties

    /// <summary>
    /// Dispatched to the game when Reference Data, World Data and Player Data
    /// are initialized.
    /// </summary>
    public UnityEvent GameReady;

    /// <summary>
    /// Reference to the main camera
    /// </summary>
    public Camera mainCamera;

    /// <summary>
    /// Reference to the server Manager, responsible for all REST calls.
    /// </summary>
    public ServerManager ServerManager;

    /// <summary>
    /// Reference to avatar
    /// </summary>
    public GameObject Avatar;

    /// <summary>
    /// Reference to ground material
    /// </summary>
    public Material GroundMaterial;

    /// <summary>
    /// Reference to roads material
    /// </summary>
    public Material RoadsMaterial;

    /// <summary>
    /// Reference to building walls material
    /// </summary>
    public Material BuildingsWallMaterial;

    /// <summary>
    /// Reference to building roof material
    /// </summary>
    public Material BuildingsRoofMaterial;

    /// <summary>
    ///   Distance inside which buildings will be completely squashed (<see cref="MaximumSquash" />)
    /// </summary>
    public float SquashNear = 50;

    /// <summary>
    ///   Distance outside which buildings will not be squashed.
    /// </summary>
    public float SquashFar = 200;

    /// <summary>
    ///   The vertical scaling factor applied at maximum squashing.
    /// </summary>
    public float MaximumSquash = 0.1f;

    /// <summary>
    /// Indicates if we have acquired a GPS Location
    /// </summary>
    [ReadOnly] [SerializeField] private bool hasGPSLocation;

    /// <summary>
    /// Indicates if the floating origin has been set
    /// </summary>
    private bool FloatingOriginIsSet;

    /// <summary>
    /// Reference to the styles options applied to loaded map features
    /// </summary>
    private GameObjectOptions ZoinkiesStylesOptions;

    /// <summary>
    /// Keeps track of startup milestones
    /// </summary>
    private List<String> StartupCheckList;

    /// <summary>
    /// Setup milestone: Reference data loaded and initialized.
    /// </summary>
    private static string REFERENCE_DATA_INITIALIZED = "REFERENCE_DATA_INITIALIZED";

    /// <summary>
    /// Setup milestone: Player data loaded and initialized.
    /// </summary>
    private static string PLAYER_DATA_INITIALIZED = "PLAYER_DATA_INITIALIZED";

    /// <summary>
    /// Setup milestone: Map data loaded and initialized.
    /// </summary>
    private static string MAP_INITIALIZED = "MAP_INITIALIZED";

    /// <summary>
    /// Indicates if the game has started
    /// </summary>
    private bool GameStarted;

    #endregion

    /// <summary>
    /// Sets up the setup milestones list.
    /// Loads reference data and player data.
    /// </summary>
    private void Awake() {

      // Initialize start up check list
      StartupCheckList = new List<string> {
        REFERENCE_DATA_INITIALIZED,
        PLAYER_DATA_INITIALIZED,
        MAP_INITIALIZED
      };

      // Load and initialize Reference Data
      ReferenceService.GetInstance().Init(ServerManager.GetReferenceData());
      StartupCheckList.Remove(REFERENCE_DATA_INITIALIZED);

      // Load and initialize Player Data
      PlayerService.GetInstance().Init(ServerManager.GetPlayerData());
      StartupCheckList.Remove(PLAYER_DATA_INITIALIZED);

      CheckStartConditions();
    }

    /// <summary>
    /// Performs the initial Map load
    /// </summary>
    protected override void Start() {
      Assert.IsNotNull(mainCamera);
      Assert.IsNotNull(ServerManager);
      Assert.IsNotNull(Avatar);
      base.Start();
      // Load Initial Map
      LoadMap();
    }

    /// <summary>
    /// Initializes the style options for this game, by setting materials to roads, buildings
    /// and water areas.
    ///
    /// </summary>
    protected override void InitStylingOptions() {
      ZoinkiesStylesOptions = ExampleDefaults.DefaultGameObjectOptions;

      // The default maps shader has a glossy property that allows the sky to reflect on it. Cool.
      Material waterMaterial = ExampleDefaults.DefaultGameObjectOptions.RegionStyle.FillMaterial;
      waterMaterial.color = new Color(0.4274509804f, 0.7725490196f, 0.8941176471f);

      ZoinkiesStylesOptions.ExtrudedStructureStyle = new ExtrudedStructureStyle.Builder {
        RoofMaterial = BuildingsRoofMaterial,
        WallMaterial = BuildingsWallMaterial
      }.Build();

      ZoinkiesStylesOptions.ModeledStructureStyle = new ModeledStructureStyle.Builder {
        Material = BuildingsWallMaterial
      }.Build();

      ZoinkiesStylesOptions.RegionStyle = new RegionStyle.Builder {
        FillMaterial = GroundMaterial
      }.Build();

      ZoinkiesStylesOptions.AreaWaterStyle = new AreaWaterStyle.Builder {
        FillMaterial = waterMaterial
      }.Build();

      ZoinkiesStylesOptions.LineWaterStyle = new LineWaterStyle.Builder {
        Material = waterMaterial
      }.Build();

      ZoinkiesStylesOptions.SegmentStyle = new SegmentStyle.Builder {
        Material = RoadsMaterial
      }.Build();

      if (RenderingStyles == null) RenderingStyles = ZoinkiesStylesOptions;
    }

    /// <summary>
    /// Adds some squashing behavior to all extruded structures.
    /// Basically, we squash everything around our Avatar so that generated game items can be seen
    /// from a distance.
    /// </summary>
    protected override void InitEventListeners() {
      base.InitEventListeners();

      if (MapsService == null) return;

      // Apply a post-creation listener that adds the squashing MonoBehaviour to each building.
      MapsService.Events.ExtrudedStructureEvents.DidCreate.AddListener(
        e => { AddSquasher(e.GameObject); });

      // Apply a post-creation listener that adds the squashing MonoBehaviour to each building.
      MapsService.Events.ModeledStructureEvents.DidCreate.AddListener(
        e => { AddSquasher(e.GameObject); });

      MapsService.Events.MapEvents.Loaded.AddListener(arg0 => {
        StartupCheckList.Remove(MAP_INITIALIZED);
        CheckStartConditions();
      });
    }

    #region event listeners

    /// <summary>
    /// Triggered by the UI when a new game needs to be created.
    /// This event listener resets player and world data.
    ///
    /// </summary>
    public void OnNewGame() {
      Debug.Log("OnNewGame+++");

      GameStarted = false;
      StartupCheckList = new List<string> {PLAYER_DATA_INITIALIZED, MAP_INITIALIZED};

      //PlayerService.GetInstance().data = new PlayerData();
      // Load and initialize Player Data
      PlayerService.GetInstance().Init(ServerManager.GetPlayerData());
      StartupCheckList.Remove(PLAYER_DATA_INITIALIZED);

      // Reload Maps
      LoadMap();
    }

    public void OnShowWorld() {
      Debug.Log("OnShowWorld+++");
      Avatar.gameObject.SetActive(true);
      GetComponent<DynamicMapsUpdater>().enabled = true;
      mainCamera.enabled = true;
    }


    #endregion

    #region utils

    // We consider that the game is loaded when:
    // Reference data and player data are initialized
    // and the Map region is loaded.
    private void CheckStartConditions() {
      if (StartupCheckList.Count == 0 && !GameStarted) {
        GameReady?.Invoke();
        GameStarted = true;
      }
    }

    /// <summary>
    ///   Adds a Squasher MonoBehaviour to the supplied GameObject.
    /// </summary>
    /// <remarks>
    ///   The Squasher MonoBehaviour reduced the vertical scale of the GameObject's transform
    ///   when a building object is nearby.
    /// </remarks>
    /// <param name="go">The GameObject to which to add the Squasher behaviour.</param>
    private void AddSquasher(GameObject go) {
      var squasher = go.AddComponent<Squasher>();
      squasher.Target = Avatar.transform;
      squasher.Near = SquashNear;
      squasher.Far = SquashFar;
      squasher.MaximumSquashing = MaximumSquash;
    }

    #endregion
  }
}
