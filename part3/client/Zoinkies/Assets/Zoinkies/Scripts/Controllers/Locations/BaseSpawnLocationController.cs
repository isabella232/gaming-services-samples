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
using Unity.Collections;
using UnityEngine;
using UnityEngine.Assertions;

namespace Google.Maps.Demos.Zoinkies {

  public class BaseSpawnLocationController : MonoBehaviour {
    [ReadOnly] public UIManager UIManager;
    [ReadOnly] public ServerManager ServerManager;

    public GameObject Model;

    public bool IsTesting = false;

    protected bool IsLoading = false;
    protected SpawnLocation location;
    protected string LocationId;

    public void Init(string LocationId) {
      if (IsTesting) {
        location = GetTestingLocation();
        this.LocationId = location.id;
      }
      else {
        this.LocationId = LocationId;
        location = WorldService.GetInstance().GetSpawnLocation(LocationId);
      }

      UIManager = GameObject.FindGameObjectWithTag("UIManager").GetComponent<UIManager>();
      ServerManager = GameObject.FindGameObjectWithTag("ServerManager")
        .GetComponent<ServerManager>();
    }

    void Start() {
      StartImpl();
    }

    void Update() {
      UpdateImpl();
    }

    protected virtual void StartImpl() {
      Assert.IsNotNull(Model);
    }

    protected virtual void UpdateImpl() {
      if (LocationId != null && WorldService.GetInstance().IsRespawning(LocationId)) {
        RespawingState();
      }
    }

    protected virtual void RespawingState() {

    }

    protected virtual void ReadyState() {

    }

    protected virtual void ActionState() {
      Debug.Log("OnClicked+++ ActioningState " + this.LocationId);
    }

    public void OnClicked() {

      // Detect how far we are from the avatar
      // We need to be within range
      GameObject avatar = GameObject.FindWithTag("Player");
      if (avatar != null) {

        float dist = Vector3.Distance(avatar.transform.position, this.transform.position);
        if (dist < 250) {
          UIManager.OnShowLoadingView(true);
          ActionState();
        }
        else {
          UIManager.OnShowMessageDialog("This location is too far away. " +
                                        "\nYou need to be within 250 meters! \nYou are "
                                        + dist.ToString("N0") + " meters away.");
        }
      }
    }

    protected virtual void OnError(string error) {
      Debug.LogError(error);
      IsLoading = false;
    }

    protected virtual SpawnLocation GetTestingLocation() {
      throw new NotImplementedException();
    }

  }
}
