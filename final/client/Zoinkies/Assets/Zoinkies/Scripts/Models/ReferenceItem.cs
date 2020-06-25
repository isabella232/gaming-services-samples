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

using System.Text;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    ///     Models a single reference item object.
    /// </summary>
    public class ReferenceItem
    {
        /// <summary>
        ///     Unique identifier for this object
        /// </summary>
        public string id { get; set; }

        /// <summary>
        ///     Friendly name for the UI
        /// </summary>
        public string name { get; set; }

        /// <summary>
        ///     Type of the object
        /// </summary>
        public string type { get; set; }

        /// <summary>
        ///     Friendly description for the UI
        /// </summary>
        public string description { get; set; }

        /// <summary>
        ///     Base Attack score (if any)
        /// </summary>
        public int attackScore { get; set; }

        /// <summary>
        ///     Base Defense score (if any)
        /// </summary>
        public int defenseScore { get; set; }

        /// <summary>
        ///     Base cooldown in between two battle turns (if any)
        /// </summary>
        public string cooldown { get; set; }

        /// <summary>
        ///     Time to respawn after use/battle (if any)
        /// </summary>
        public string respawnDuration { get; set; }

        /// <summary>
        ///     Name of the prefab to instantiate in the editor.
        /// </summary>
        public string prefab { get; set; }

        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("Id: " + id);
            sb.Append("Name: " + name);
            sb.Append("Type: " + type);
            sb.Append("Description: " + description);
            sb.Append("AttackScore: " + attackScore);
            sb.Append("DefenseScore: " + defenseScore);
            sb.Append("Cooldown: " + cooldown);
            sb.Append("RespawnDuration: " + respawnDuration);
            sb.Append("Prefab: " + prefab);
            return sb.ToString();
        }
    }
}
