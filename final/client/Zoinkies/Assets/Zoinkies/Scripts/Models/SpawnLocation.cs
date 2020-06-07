using System;

namespace Google.Maps.Demos.Zoinkies {

    public class SpawnLocation {
        /// <summary>
        /// Unique identifier for the location
        /// </summary>
        public string id { get; set; }
        /// <summary>
        /// Indicates if the location is active
        /// </summary>
        public bool active { get; set; }
        /// <summary>
        /// The type of object at this location
        /// </summary>
        public string object_type_id { get; set; }
        /// <summary>
        /// Indicates if the location respawns
        /// </summary>
        public bool respawns { get; set; }
        /// <summary>
        /// Indicates when the location respawns (ISO 8601)
        /// </summary>
        public string respawn_time { get; set; }
        /// <summary>
        /// Number of "keys" to activate this location (if any)
        /// </summary>
        public int number_of_keys_to_activate { get; set; }
        /// <summary>
        /// The key type for activating this location - could be anything.
        /// </summary>
        public string key_type_id { get; set; }
        /// <summary>
        /// Actual lat lng for this location.
        /// </summary>
        public PLLatLng snappedPoint { get; set; }

        public SpawnLocation() {}

        public SpawnLocation(string id,
            Boolean active,
            string object_type_id,
            bool respawns,
            string respawn_time,
            int number_of_keys_to_activate,
            string key_type_id,
            PLLatLng snappedPoint) {

            this.id = id;
            this.active = active;
            this.object_type_id = object_type_id;
            this.respawns = respawns;
            this.key_type_id = key_type_id;
            this.respawn_time = respawn_time;
            this.number_of_keys_to_activate = number_of_keys_to_activate;
            this.snappedPoint = snappedPoint;
        }

        /*
        public void CopyAttributes(SpawnLocation sl) {
            if (sl != null) {
                id = sl.id;
                active = sl.active;
                object_type_id = sl.object_type_id;
                respawns = sl.respawns;
                key_type_id = sl.key_type_id;
                respawn_time = sl.respawn_time;
                number_of_keys_to_activate = sl.number_of_keys_to_activate;
                snappedPoint = sl.snappedPoint;
            }
        }
        */
    }
}
