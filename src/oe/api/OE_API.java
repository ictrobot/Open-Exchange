package oe.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oe.api.lib.Location;
import oe.api.lib.OEType;

public class OE_API {
  
  /**
   * Checks if a class extends OEInterface (Checks by looking for a method called "isOE")
   * 
   * @param c
   * @return
   */
  public static boolean isOE(Class<?> c) {
    /*
     * try {
     * Method method = c.getDeclaredMethod("isOE", Object.class);
     * return method != null;
     * } catch (Exception e) {
     * return false;
     * }
     */
    Class<?>[] classes = c.getInterfaces();
    for (Class<?> i : classes) {
      if (i.getName().contains("OEItemInterface") || i.getName().contains("OETileInterface")) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Checks if OE pipe
   * 
   * @param c
   * @return
   */
  public static boolean isOEPipe(Class<?> c) {
    Class<?>[] classes = c.getInterfaces();
    for (Class<?> i : classes) {
      if (i.getName().contains("OEPipeInterface")) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Checks if a class extends OEGuessable (Checks by looking for a method called "isOEGuessable")
   * 
   * @param c
   * @return
   */
  public static boolean isOEGuessable(Class<?> c) {
    Class<?> e = c.getSuperclass();
    if (e == null) {
      return false;
    }
    return e.getName().contains("OEGuesser");
  }
  
  /**
   * Provide other blocks with QMC, Coords are for the block providing
   * 
   * @param x
   * @param y
   * @param z
   * @param qmc
   * @return amount accepted qmc
   */
  public static double provide(int X, int Y, int Z, World world, double qmc) {
    if (qmc == 0) {
      return 0;
    }
    double qmcLeftOver = qmc;
    Location[] toCheck = getProvideLocations(X, Y, Z, world);
    for (Location loc : toCheck) {
      int x = (int) loc.x;
      int y = (int) loc.y;
      int z = (int) loc.z;
      int[] tier = new int[6];
      double[] canHandle = new double[6];
      for (int c = 0; c < 6; c++) {
        tier[c] = 0;
        canHandle[c] = 0;
        int targetX = x;
        int targetY = y;
        int targetZ = z;
        switch (c) {
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
        }
        TileEntity tile = world.getBlockTileEntity(targetX, targetY, targetZ);
        if (tile != null) {
          if (isOE(tile.getClass())) {
            OETileInterface oe = (OETileInterface) tile;
            if (oe.getType() != OEType.Producer && oe.getType() != OEType.None) {
              tier[c] = oe.getTier();
              canHandle[c] = oe.getMaxQMC() - oe.getQMC();
            }
          }
        }
      }
      int maxTier = 0;
      for (int i = 0; i < 6; i++) {
        if (tier[i] > maxTier) {
          maxTier = tier[i];
        }
      }
      for (int t = maxTier; t > 0; t--) {
        for (int a = 0; a < 6; a++) {
          if (tier[a] == t) {
            double amount = 0;
            if (canHandle[a] >= t * 10) {
              amount = t * 10;
            } else {
              amount = canHandle[a];
            }
            if (amount > qmcLeftOver) {
              amount = qmcLeftOver;
            }
            int targetX = x;
            int targetY = y;
            int targetZ = z;
            switch (a) {
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
            }
            TileEntity tile = world.getBlockTileEntity(targetX, targetY, targetZ);
            if (tile != null) {
              if (isOE(tile.getClass())) {
                if (targetX != X || targetY != Y || targetZ != Z) {
                  OETileInterface oe = (OETileInterface) tile;
                  oe.increaseQMC(amount);
                  qmcLeftOver = qmcLeftOver - amount;
                }
              }
            }
          }
        }
      }
    }
    return qmcLeftOver;
  }
  
  public static Location[] getProvideLocations(int x, int y, int z, World world) {
    Location[] loc = new Location[1];
    loc[0] = new Location(x, y, z);
    loc = getPipes(x, y, z, world, loc, 0);
    return loc;
  }
  
  private static Location[] getPipes(int x, int y, int z, World world, Location[] checkedLoc, int recursions) {
    recursions++;
    if (recursions == 100) {
      return checkedLoc;
    }
    checkedLoc = checkedLoc.clone();
    for (int c = 0; c < 6; c++) {
      int targetX = x;
      int targetY = y;
      int targetZ = z;
      switch (c) {
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
      }
      TileEntity tile = world.getBlockTileEntity(targetX, targetY, targetZ);
      if (tile != null) {
        if (isOEPipe(tile.getClass())) {
          boolean checked = false;
          for (Location loc : checkedLoc) {
            if (loc.x == targetX && loc.y == targetY && loc.z == targetZ) {
              checked = true;
              break;
            }
          }
          if (!checked) {
            Location[] tmp = new Location[checkedLoc.length + 1];
            System.arraycopy(checkedLoc, 0, tmp, 0, checkedLoc.length);
            checkedLoc = tmp;
            checkedLoc[checkedLoc.length - 1] = new Location(targetX, targetY, targetZ);
            checkedLoc = getPipes(targetX, targetY, targetZ, world, checkedLoc, recursions + 1);
          }
        }
      }
    }
    return checkedLoc;
  }
}
