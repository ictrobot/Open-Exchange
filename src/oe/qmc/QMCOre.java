package oe.qmc;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class QMCOre {
  
  private static QMCOreData[] data = new QMCOreData[1];
  
  public static void load() {
    DefaultOreQMC.load();
  }
  
  public static int length() {
    return data.length;
  }
  
  private static void increase() {
    QMCOreData[] tmp = new QMCOreData[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
  }
  
  public static void add(String oreDictionary, double value) {
    data[data.length - 1] = new QMCOreData(oreDictionary, value);
    increase();
  }
  
  public static double getQMC(Item item) {
    return getQMC(new ItemStack(item));
  }
  
  public static double getQMC(Block block) {
    return getQMC(new ItemStack(block));
  }
  
  public static double getQMC(ItemStack itemstack) {
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
