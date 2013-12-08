package oe.core.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import oe.block.Blocks;

public class QuantumToolBlackList {
  
  private static List<Integer> blockIDs = new ArrayList<Integer>();
  
  public static void init() {
    add(Block.bedrock);
    add(Blocks.drill);
    add(Blocks.drillRemote);
  }
  
  public static void add(Block block) {
    add(block.blockID);
  }
  
  public static void add(int blockID) {
    if (blockID > 0) {
      blockIDs.add(blockID);
    }
  }
  
  public static boolean isBlackListed(Block block) {
    return isBlackListed(block.blockID);
  }
  
  public static boolean isBlackListed(int blockID) {
    return blockIDs.contains(blockID);
  }
}
