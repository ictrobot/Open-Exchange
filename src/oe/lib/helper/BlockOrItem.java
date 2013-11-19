package oe.lib.helper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class BlockOrItem {
  public static boolean isItem(int ID) {
    if (isBlock(ID)) {
      return false;
    }
    if (Item.itemsList[ID] == null) {
      return false;
    }
    return true;
  }
  
  public static boolean isBlock(int ID) {
    if (ID > 4096) {
      return false;
    }
    if (Block.blocksList[ID].getUnlocalizedName().contains("ForgeFiller")) {
      return false;
    }
    if (Block.blocksList[ID] == null) {
      return false;
    }
    return true;
  }
}
