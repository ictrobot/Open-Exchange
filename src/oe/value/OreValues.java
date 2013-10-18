package oe.value;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreValues {
  
  private static OreData[] data = new OreData[1];
  
  public static void load() {
    DefaultOre.load();
  }
  
  public static int length() {
    return data.length;
  }
  
  private static void increase() {
    OreData[] tmp = new OreData[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
  }
  
  public static void add(String oreDictionary, int value) {
    data[data.length - 1] = new OreData(oreDictionary, value);
    increase();
  }
  
  public static int getValue(Item item) {
    return getValue(new ItemStack(item));
  }
  
  public static int getValue(Block block) {
    return getValue(new ItemStack(block));
  }
  
  public static int getValue(ItemStack itemstack) {
    if (itemstack == null) {
      return -1;
    }
    int s = OreDictionary.getOreID(itemstack);
    String ore;
    if (s == -1) {
      return -1;
    } else {
      ore = OreDictionary.getOreName(s);
    }
    for (int i = 1; i < data.length; i++) {
      if (data[i - 1].oreDictionaryName == ore) {
        return data[i - 1].value;
      }
    }
    return -1;
  }
}
