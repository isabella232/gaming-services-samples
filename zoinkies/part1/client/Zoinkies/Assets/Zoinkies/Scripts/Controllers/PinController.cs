using UnityEngine;
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

using Unity.Collections;

namespace Google.Maps.Demos.Zoinkies
{
    public sealed class PinController : MonoBehaviour
    {
        /// <summary>
        /// The avatar type
        /// </summary>
        [ReadOnly]
        public string AvatarType = "";

        public void Update()
        {
           if (PlayerService.GetInstance().IsInitialized)
           {
               ReferenceItem refItem = ReferenceService.GetInstance()
                   .GetItem(PlayerService.GetInstance().AvatarType);
               if (refItem != null && refItem.itemId != AvatarType)
               {
                   Material pinMaterial = (Material) Resources.Load("Material" + refItem.prefab);
                   GetComponent<MeshRenderer>().sharedMaterial = pinMaterial;
                   AvatarType = refItem.itemId;
               }
           }
        }
    }
}
