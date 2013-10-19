package oe.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class OEProvideQMC {
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
    double qmcLeftOver = qmc;
    int[] tier = new int[6];
    double[] canHandle = new double[6];
    for (int i = 0; i < 6; i++) {
      tier[i] = 0;
      canHandle[i] = 0;
      int targetX = x;
      int targetY = y;
      int targetZ = z;
      switch (i) {
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
      TileEntity te = world.getBlockTileEntity(targetX, targetY, targetZ);
      if (te != null) {
        if (te instanceof OETileInterface) {
          OETileInterface oe = (OETileInterface) te;
          tier[i] = oe.getTier();
          canHandle[i] = oe.getMaxQMC() - oe.getQMC();
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
      for (int i = 0; i < 5; i++) {
        if (tier[i] == maxTier) {
          double amount = 0;
          if (canHandle[i] >= maxTier * 10) {
            amount = maxTier * 10;
          } else {
            amount = canHandle[i];
          }
          if (amount > qmcLeftOver) {
            amount = qmcLeftOver;
          }
          int targetX = x;
          int targetY = y;
          int targetZ = z;
          switch (i) {
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
          TileEntity te = world.getBlockTileEntity(targetX, targetY, targetZ);
          if (te != null) {
            if (te instanceof OETileInterface) {
              OETileInterface oe = (OETileInterface) te;
              oe.increaseQMC(amount);
              qmcLeftOver = qmcLeftOver - amount;
            }
          }
        }
      }
    }
    return qmcLeftOver;
  }
}
