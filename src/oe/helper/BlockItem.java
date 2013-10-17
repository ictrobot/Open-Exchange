package oe.helper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class BlockItem {
  public static boolean isItem(int ID) {
    return Item.itemsList[ID] != null;
  }
  
  public static boolean isBlock(int ID) {
    if (ID > 4096) {
      return false;
    }
    if (Block.blocksList[ID].getUnlocalizedName() == "ForgeFiller") {
      return false;
    }
    return true;
  }
}
