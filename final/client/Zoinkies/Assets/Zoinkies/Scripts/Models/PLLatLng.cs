namespace Google.Maps.Demos.Zoinkies {

  /// <summary>
  /// Simple class to keep track of a latitude longitude pair.
  /// </summary>
  public class PLLatLng {
    public double latitude { get; set; }
    public double longitude { get; set; }

    public override string ToString() {
      return "latitude: " + latitude + "longitude: " + longitude;
    }
  }
}
