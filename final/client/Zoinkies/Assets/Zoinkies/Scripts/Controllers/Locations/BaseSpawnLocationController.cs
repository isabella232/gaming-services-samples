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

    protected string PlaceId;

    public void Init(string PlaceId) {

      if (IsTesting) {
        location = GetTestingLocation();
        this.PlaceId = location.id;
      }
      else {
        this.PlaceId = PlaceId;
        location = WorldService.GetInstance().GetSpawnLocation(PlaceId);
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

      Init(name); // The name property is equal to the placeId TODO - fragile - call Init at instantiation time
    }

    protected virtual void UpdateImpl() {
      if (PlaceId != null && WorldService.GetInstance().IsRespawning(PlaceId)) {
        RespawingState();
      }
    }

    protected virtual void RespawingState() {

    }

    protected virtual void ReadyState() {

    }

    protected virtual void ActionState() {
      Debug.Log("OnClicked+++ ActioningState " + this.PlaceId);
      Init(name); // The name property is equal to the placeId TODO - fragile - call Init at instantiation time
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
