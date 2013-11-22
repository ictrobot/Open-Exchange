package oe.lib.util;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStackUtil {
  
  public static ItemStack getItemStackFromObject(Object o) {
    if (o instanceof ItemStack) {
      ItemStack stack = (ItemStack) o;
      return stack;
    } else if (o instanceof String) {
      if (((String) o).startsWith("liquid$")) {
        String fluid = ((String) o).substring(7);
        ItemStack[] fStacks = FluidUtil.getStoringItemStacks(FluidUtil.getFluidID(fluid));
        return getSingleItemStack(fStacks, "bucket"); // Prefer buckets
      }
      String ore = (String) o;
      ItemStack[] stacks = OreDictionaryUtil.getItemStacks(ore);
      if (stacks != null) {
        return stacks[0];
      }
    } else if (o instanceof ArrayList) {
      return getItemStackFromObject(((ArrayList<?>) o).toArray()[0]);
    }
    return null;
  }
  
  public static ItemStack getSingleItemStack(ItemStack[] stacks) {
    return getSingleItemStack(stacks, "");
  }
  
  public static ItemStack getSingleItemStack(ItemStack[] stacks, String str) {
    // Return first matching
    for (ItemStack itemstack : stacks) {
      if (itemstack != null && itemstack.getUnlocalizedName().contains(str)) {
        return itemstack;
      }
    }
    // Return first that is not null
    for (ItemStack itemstack : stacks) {
      if (itemstack != null) {
        return itemstack;
      }
    }
    
    return null;
  }
  
  public static ItemStack[] increaseAdd(ItemStack[] array, ItemStack toAdd) {
    if (array == null || toAdd == null) {
      return array;
    }
    ItemStack[] tmp = new ItemStack[array.length + 1];
    System.arraycopy(array, 0, tmp, 0, array.length);
    array = tmp;
    array[array.length - 1] = toAdd;
    return array;
  }
  
  public static boolean isValidTool(ItemStack o) {
    return o.getItem().isItemTool(o);
  }
  
  public static boolean isItem(int ID) {
    if (ID >= 32000) {
      return false;
    }
    if (isBlock(ID)) {
      return false;
    }
    if (Item.itemsList[ID] == null) {
      return false;
    }
    return true;
  }
  
  public static boolean isBlock(int ID) {
    if (ID >= 4096) {
      return false;
    }
    if (Block.blocksList[ID] == null) {
      return false;
    }
    if (Block.blocksList[ID].getUnlocalizedName().contains("ForgeFiller")) {
      return false;
    }
    return true;
  }
}
