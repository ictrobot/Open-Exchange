package oe.api.lib;

public class Location {
  public double x;
  public double y;
  public double z;
  public int worldID;
  
  public Location(double X, double Y, double Z) {
    this.x = X;
    this.y = Y;
    this.z = Z;
  }
  
  public Location(double X, double Y, double Z, int WorldID) {
    this.x = X;
    this.y = Y;
    this.z = Z;
    this.worldID = WorldID;
  }
}
