package oe.lib.util;

import oe.lib.misc.BlockLocation;

public class WorldUtil {
  
  public static BlockLocation getLocationOnSide(int x, int y, int z, int side) {
    int targetX = x;
    int targetY = y;
    int targetZ = z;
    switch (side) {
      case 0:
        targetY++;
        break;
      case 1:
        targetY--;
        break;
      case 2:
        targetZ++;
        break;
      case 3:
        targetZ--;
        break;
      case 4:
        targetX++;
        break;
      case 5:
        targetX--;
        break;
      default:
        break;
    }
    return new BlockLocation(targetX, targetY, targetZ);
  }
  
  public static boolean exists(BlockLocation[] b, BlockLocation toCheck) {
    for (BlockLocation loc : b) {
      if (loc.x == toCheck.x && loc.y == toCheck.y && loc.z == toCheck.z) {
        return true;
      }
    }
    return false;
  }
  
  public static BlockLocation[] increaseAdd(BlockLocation[] array, BlockLocation toAdd) {
    if (array == null || toAdd == null) {
      return array;
    }
    BlockLocation[] tmp = new BlockLocation[array.length + 1];
    System.arraycopy(array, 0, tmp, 0, array.length);
    array = tmp;
    array[array.length - 1] = toAdd;
    return array;
  }
}
