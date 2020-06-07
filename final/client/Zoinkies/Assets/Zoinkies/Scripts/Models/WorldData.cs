using System;
using System.Collections.Generic;
using System.Text;

namespace Google.Maps.Demos.Zoinkies {

    /// <summary>
    /// Keeps track of all game locations.
    /// </summary>
    public class WorldData {
        /// <summary>
        /// List of spawn locations
        /// </summary>
        public Dictionary<string, SpawnLocation> locations { get; set; }

        /// <summary>
        /// Current server time
        /// </summary>
        public string currentServerTime { get; set; }

        public WorldData() {
            locations = new Dictionary<string, SpawnLocation>();
            currentServerTime = DateTime.UtcNow.ToString();
        }

        public override string ToString() {
            StringBuilder sb = new StringBuilder();
            sb.Append("Locations: \n");
            foreach (SpawnLocation loc in locations.Values) {
                sb.Append(loc.ToString() + " \t");
            }

            sb.Append("----------------------");

            return sb.ToString();
        }

        /*
        public void CopyAttributes(WorldData newData) {
            this.currentServerTime = newData.currentServerTime;
            foreach (String slkey in newData.locations.Keys) {
                if (locations.ContainsKey(slkey)) {
                    locations[slkey].CopyAttributes(newData.locations[slkey]);
                }
                else {
                    locations.Add(slkey, newData.locations[slkey]);
                }
            }
        }
        */
    }
}
