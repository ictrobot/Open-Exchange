package oe.core.data;

public class BlockLocation {
  public int x;
  public int y;
  public int z;
  public int worldID;

  public BlockLocation(int X, int Y, int Z) {
    this.x = X;
    this.y = Y;
    this.z = Z;
  }

  public BlockLocation(int X, int Y, int Z, int WorldID) {
    this.x = X;
    this.y = Y;
    this.z = Z;
    this.worldID = WorldID;
  }
}
