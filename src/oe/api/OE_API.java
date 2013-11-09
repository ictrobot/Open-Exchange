package oe.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
      if (i.getName().contains("OE") && i.getName().contains("Interface")) {
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
  public static double provide(int x, int y, int z, World world, double qmc) {
    if (!hasTarget(x, y, z, world)) {
      return qmc;
    }
    double qmcLeftOver = qmc;
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
        if (tier[a] == maxTier) {
          double amount = 0;
          if (canHandle[a] >= maxTier * 10) {
            amount = maxTier * 10;
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
              OETileInterface oe = (OETileInterface) tile;
              oe.increaseQMC(amount);
              qmcLeftOver = qmcLeftOver - amount;
            }
          }
        }
      }
    }
    return qmcLeftOver;
  }
  
  public static boolean hasTarget(int x, int y, int z, World world) {
    boolean[] target = targets(x, y, z, world);
    boolean hastarget = false;
    for (int c = 0; c < target.length; c++) {
      if (target[c]) {
        hastarget = true;
        break;
      }
    }
    return hastarget;
  }
  
  public static boolean[] targets(int x, int y, int z, World world) {
    boolean[] target = new boolean[6];
    for (int c = 0; c < 6; c++) {
      target[c] = false;
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
          target[c] = true;
        }
      }
    }
    return target;
  }
}
