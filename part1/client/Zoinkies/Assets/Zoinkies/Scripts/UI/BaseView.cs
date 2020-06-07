using UnityEngine;
using UnityEngine.Events;

namespace Google.Maps.Demos.Zoinkies {

    /// <summary>
    /// Base class for all views.
    /// Provides support for an OnClose event.
    /// </summary>
    public class BaseView : MonoBehaviour {
        public UnityAction OnClose;
    }
}
