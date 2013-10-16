package oe.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oe.value.*;

public class OEValue {
  
  public static void setValue(ItemStack itemstack, int value) {
    Values.add(itemstack, value);
  }
  
  public static int getValue(ItemStack itemstack) {
    return Values.getValue(itemstack);
  }
  
  public static int getValue(Block block) {
    return Values.getValue(block);
  }
  
  public static int getValue(Item item) {
    return Values.getValue(item);
  }
  
  public static String getName() {
    return Values.name;
  }
  
  public static String getNameFull() {
    return Values.nameFull;
  }
}
