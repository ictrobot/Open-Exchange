package oe.helper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class BlockOrItem {
  public static boolean isItem(int ID) {
    return Item.itemsList[ID] != null;
  }
  
  public static boolean isBlock(int ID) {
    return Block.blocksList[ID] != null;
  }
}
