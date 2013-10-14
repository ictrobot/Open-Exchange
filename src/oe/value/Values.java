package oe.value;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oe.Log;

public class Values {
  
  public final static String name = "QMC";
  public final static String nameFull = "Quantum Matter Currency";
  
  private static ValueData[] data = new ValueData[1];
  
  public static void load() {
  }
  
  public static int Length() {
    return data.length;
  }
  
  public static int getValue(Item item) {
    for (int i = 1; i < data.length; i++) {
      if (data[i - 1].type == 1) {
        if (data[i - 1].item == item) {
          return data[i - 1].value;
        }
      }
    }
    return 0;
  }
  
  public static int getValue(Block block) {
    for (int i = 1; i < data.length; i++) {
      if (data[i - 1].type == 0) {
        if (data[i - 1].block == block) {
          return data[i - 1].value;
        }
      }
    }
    return 0;
  }
  
  public static int getValue(ItemStack itemstack) {
    if (itemstack == null) {
      return -1;
    }
    itemstack.stackSize = 1;
    for (int i = 1; i < data.length; i++) {
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
    return 0;
  }
  
  public static void increase() {
    ValueData[] tmp = new ValueData[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
  }
  
  public static void add(Block block2, int Value) {
    data[data.length - 1] = new ValueData(block2, Value);
    increase();
  }
  
  public static void add(ItemStack stack, int Type, int Value) {
    data[data.length - 1] = new ValueData(stack, Type, Value);
    increase();
  }
  
  public static void add(Block block2, int meta, int Value) {
    data[data.length - 1] = new ValueData(block2, meta, Value);
    increase();
  }
  
  public static void add(Block block2, int meta, ItemStack stack, int Value) {
    data[data.length - 1] = new ValueData(block2, meta, stack, Value);
    increase();
  }
  
  public static void add(Block block2, ItemStack stack, int Value) {
    data[data.length - 1] = new ValueData(block2, stack, Value);
    increase();
  }
  
  public static void add(Item item2, int meta, ItemStack stack, int Value) {
    data[data.length - 1] = new ValueData(item2, meta, stack, Value);
    increase();
  }
  
  public static void add(Item item2, int meta, int Value) {
    data[data.length - 1] = new ValueData(item2, meta, Value);
    increase();
  }
  
  public static void add(Item item2, int Value) {
    data[data.length - 1] = new ValueData(item2, Value);
    increase();
  }
  
  public static void add(Item item2, ItemStack stack, int Value) {
    data[data.length - 1] = new ValueData(item2, stack, Value);
    increase();
  }
  
  public static boolean hasValue(Item item) {
    return getValue(item) != 0;
  }
  
  public static boolean hasValue(Block block) {
    return getValue(block) != 0;
  }
  
  public static boolean hasValue(ItemStack itemstack) {
    boolean state = itemstackType(itemstack) != -1;
    return state;
  }
  
  private static int itemstackType(ItemStack itemstack) {
    if (itemstack == null) {
      return -1;
    }
    try {
      for (int i = 1; i < data.length; i++) {
        if (itemstack.itemID == data[i - 1].ID) {
          return data[i - 1].type;
        }
      }
    } catch (NullPointerException e) {
      return -1;
    }
    return -1;
  }
}
