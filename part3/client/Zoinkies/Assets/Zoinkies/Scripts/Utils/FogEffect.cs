using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace Google.Maps.Demos.Zoinkies {

    public class FogEffect : MonoBehaviour {
        void Awake() {
            // called on script load
            // see https://docs.unity3d.com/ScriptReference/MonoBehaviour.Awake.html
        }

        void OnValidate() {
            // called when some editor value is changed
            // see https://docs.unity3d.com/ScriptReference/MonoBehaviour.OnValidate.html
        }

        void OnRenderImage(RenderTexture source, RenderTexture destination) {
            // copy source to destination without any changes
            Graphics.Blit(source, destination);
        }
    }
}
