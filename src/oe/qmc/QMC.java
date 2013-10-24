package oe.qmc;

import java.text.DecimalFormat;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import oe.Log;
import oe.helper.ConfigHelper;

public class QMC {
  
  public static boolean loaded = false;
  
  public static String name = "QMC";
  public static String nameFull = "Quantum Matter Currency";
  
  private static QMCData[] data = new QMCData[1];
  
  public static DecimalFormat formatter = new DecimalFormat("0.00");
  
  public static void load() {
    ConfigHelper.load();
    name = ConfigHelper.other("QMC", "Name", "QMC");
    nameFull = ConfigHelper.other("QMC", "Stands For", "Quantum Matter Currency");
    ConfigHelper.save();
    NormalQMCValues.load();
    loaded = true;
  }
  
  public static int length() {
    return data.length;
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
    for (int i = 1; i < data.length; i++) {
      if (data[i - 1].type != -1) {
        if (data[i - 1].itemstack == null) {
          Log.warning("Values Database Corrupted");
        }
        if (data[i - 1].itemstack.itemID == itemstack.itemID && (data[i - 1].itemstack.getItemDamage() == itemstack.getItemDamage() || !data[i - 1].metaprovided)) {
          if (data[i - 1].itemstack.getTagCompound() != null && itemstack.getTagCompound() != null) {
            return data[i - 1].value;
          } else {
            if (data[i - 1].itemstack.getTagCompound() == null) {
              return data[i - 1].value;
            } else if (data[i - 1].itemstack.getTagCompound() == itemstack.getTagCompound()) {
              return data[i - 1].value;
            }
          }
        }
      }
      if (data[i - 1].hasOre) {
        int oreID = OreDictionary.getOreID(itemstack);
        if (oreID != -1) {
          if (data[i - 1].ore == OreDictionary.getOreName(oreID)) {
            return data[i - 1].value;
          }
        }
      }
    }
    return -1;
  }
  
  private static void increase() {
    QMCData[] tmp = new QMCData[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
  }
  
  public static void add(String ore, double Value) {
    data[data.length - 1] = new QMCData(ore, Value);
    increase();
  }
  
  public static void add(Block block2, double Value) {
    data[data.length - 1] = new QMCData(block2, Value);
    increase();
  }
  
  public static void add(ItemStack stack, double Value) {
    data[data.length - 1] = new QMCData(stack, Value);
    increase();
  }
  
  public static void addMeta(ItemStack stack, double Value) {
    data[data.length - 1] = new QMCData(stack, Value, true);
    increase();
  }
  
  public static void add(Block block2, int meta, double Value) {
    data[data.length - 1] = new QMCData(block2, meta, Value);
    increase();
  }
  
  public static void add(Block block2, int meta, ItemStack stack, double Value) {
    data[data.length - 1] = new QMCData(block2, meta, stack, Value);
    increase();
  }
  
  public static void add(Block block2, ItemStack stack, double Value) {
    data[data.length - 1] = new QMCData(block2, stack, Value);
    increase();
  }
  
  public static void add(Item item2, int meta, ItemStack stack, double Value) {
    data[data.length - 1] = new QMCData(item2, meta, stack, Value);
    increase();
  }
  
  public static void add(Item item2, int meta, double Value) {
    data[data.length - 1] = new QMCData(item2, meta, Value);
    increase();
  }
  
  public static void add(Item item2, double Value) {
    data[data.length - 1] = new QMCData(item2, Value);
    increase();
  }
  
  public static void add(Item item2, ItemStack stack, double Value) {
    data[data.length - 1] = new QMCData(item2, stack, Value);
    increase();
  }
  
  public static boolean hasValue(Item item) {
    return getQMC(item) != -1;
  }
  
  public static boolean hasValue(Block block) {
    return getQMC(block) != -1;
  }
  
  public static boolean hasValue(ItemStack itemstack) {
    return getQMC(itemstack) != -1;
  }
}
