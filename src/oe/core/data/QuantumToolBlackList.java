package oe.core.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class QuantumToolBlackList {
  
  private static List<Integer> blockIDs = new ArrayList<Integer>();
  
  public static void init() {
    add(Blocks.bedrock);
  }
  
  public static void add(Block block) {
    blockIDs.add(Block.getIdFromBlock(block));
  }
  
  public static boolean isBlackListed(Block block) {
    return blockIDs.contains(Block.getIdFromBlock(block));
  }
}
