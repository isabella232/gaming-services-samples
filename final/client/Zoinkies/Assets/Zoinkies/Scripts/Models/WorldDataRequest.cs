using Google.Maps.Coord;

namespace Google.Maps.Demos.Zoinkies {

  public class WorldDataRequest {
    public PLLatLng northeast { get; set; }
    public PLLatLng southwest { get; set; }

    public void CopyFrom(LatLng southwestlatLng, LatLng northeastlatLng) {
      northeast = new PLLatLng();
      northeast.latitude = northeastlatLng.Lat;
      northeast.longitude = northeastlatLng.Lng;
      southwest = new PLLatLng();
      southwest.latitude = southwestlatLng.Lat;
      southwest.longitude = southwestlatLng.Lng;
    }

    public override string ToString() {
      return "northeast: " + northeast + " southwest: " + southwest;
    }
  }
}
