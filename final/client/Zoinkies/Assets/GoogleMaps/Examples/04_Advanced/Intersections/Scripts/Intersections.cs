using System.Collections.Generic;
using Google.Maps.Event;
using Google.Maps.Feature.Style;
using Google.Maps.Unity.Intersections;
using UnityEngine;

namespace Google.Maps.Examples {
  /// <summary>
  /// Applies materials to intersections and removes 3-way intersections.
  /// </summary>
  [RequireComponent(typeof(MapsService))]
  public class Intersections : MonoBehaviour {
    /// <summary>
    /// The default material to apply to intersections.
    /// </summary>
    public Material DefaultMaterial;

    /// <summary>
    /// The possible materials to apply to intersection arms.
    /// </summary>
    public Material[] ArmMaterials;

    void Awake() {
      // Subscribe to intersection events.
      GetComponent<MapsService>().Events.IntersectionEvents.WillCreate
          .AddListener(OnWillCreateIntersection);
    }

    void OnWillCreateIntersection(WillCreateIntersectionArgs args) {
      // Set the default material.
      args.Style = new SegmentStyle.Builder(args.Style) {
          IntersectionMaterial = DefaultMaterial
      }.Build();

      // Get the intersection arms from the feature shape.
      List<IntersectionArm> arms = new List<IntersectionArm>(args.MapFeature.Shape.Arms);

      if (arms.Count != 3) {
        // Not a 3-way intersection. Apply some random arm materials and return.
        foreach (IntersectionArm arm in arms) {
          // Don't change the material of two-way arms. These arms are treated as part of the inner
          // polygon (i.e. the center of the intersection where roads overlap) and should use the
          // default intersection material.
          if (arm.IsTwoWay(args.Style.IntersectionJoinLength)) {
            continue;
          }

          arm.Material = ArmMaterials[Random.Range(0, ArmMaterials.Length)];
        }
      } else {
        // Delete 3-way intersections.
        args.Cancel = true;
      }
    }
  }
}
