package oe.lib.misc;

import net.minecraft.block.Block;
import oe.block.Blocks;

public class QuantumToolBlackList {
  
  private static int[] blockIDs = new int[0];
  
  public static void init() {
    add(Block.bedrock);
    add(Blocks.drill);
    add(Blocks.drillRemote);
  }
  
  public static void add(Block block) {
    add(block.blockID);
  }
  
  public static void add(int blockID) {
    int[] tmp = new int[blockIDs.length + 1];
    System.arraycopy(blockIDs, 0, tmp, 0, blockIDs.length);
    blockIDs = tmp;
    blockIDs[blockIDs.length - 1] = blockID;
  }
  
  public static boolean isBlackListed(Block block) {
    return isBlackListed(block.blockID);
  }
  
  public static boolean isBlackListed(int blockID) {
    for (int id : blockIDs) {
      if (id == blockID) {
        return true;
      }
    }
    return false;
  }
}
