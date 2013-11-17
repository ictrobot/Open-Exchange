package oe.lib;

import oe.block.Blocks;
import net.minecraft.block.Block;

public class QuantumToolBlackList {
  private static Block[] blocks = new Block[0];
  
  public static void init() {
    add(Block.bedrock);
    add(Blocks.drill);
    add(Blocks.drillRemote);
  }
  
  public static void add(Block block) {
    Block[] tmp = new Block[blocks.length + 1];
    System.arraycopy(blocks, 0, tmp, 0, blocks.length);
    blocks = tmp;
    blocks[blocks.length - 1] = block;
  }
  
  public static boolean isBlackListed(Block block) {
    for (Block block2 : blocks) {
      if (block2.equals(block)) {
        return true;
      }
    }
    return false;
  }
}
