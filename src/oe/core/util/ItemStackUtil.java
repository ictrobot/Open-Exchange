package oe.core.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackUtil {
  
  /**
   * Get an Single ItemStack from a object
   */
  public static ItemStack getItemStack(Object o) {
    List<ItemStack> data = getItemStacks(o);
    if (Util.notEmpty(data)) {
      return data.get(0);
    }
    return null;
  }
  
  /**
   * Get all the ItemStacks from a object
   */
  public static List<ItemStack> getItemStacks(Object o) {
    List<ItemStack> data = new ArrayList<ItemStack>();
    if (o instanceof ItemStack) {
      data.add((ItemStack) o);
    } else if (o instanceof String) {
      if (((String) o).startsWith("liquid$")) {
        String fluid = ((String) o).substring(7);
        data.addAll(FluidUtil.getStoringItemStacks(FluidUtil.getFluidID(fluid)));
      } else {
        data.addAll(OreDictionaryUtil.getItemStacks((String) o));
      }
    } else if (o instanceof List) {
      for (Object object : (List<?>) o) {
        data.addAll(getItemStacks(object));
      }
    } else if (o.getClass().isArray()) {
      for (Object object : (Object[]) o) {
        data.addAll(getItemStacks(object));
      }
    }
    return data;
  }
  
  /**
   * The same as getItemStacks, but only return one ItemStack if it is an ore dictionary value
   */
  public static List<ItemStack> getItemStacksOneOre(Object o) {
    List<ItemStack> data = new ArrayList<ItemStack>();
    List<String> ore = new ArrayList<String>();
    for (ItemStack itemstack : getItemStacks(o)) {
      if (OreDictionaryUtil.isOre(itemstack)) {
        if (!ore.contains(OreDictionaryUtil.ore(itemstack))) {
          ore.add(OreDictionaryUtil.ore(itemstack));
          data.add(itemstack);
        }
      } else {
        data.add(itemstack);
      }
    }
    return data;
  }
  
  public static ItemStack first(List<?> list) {
    for (Object o : list) {
      if (o != null && o instanceof ItemStack) {
        return (ItemStack) o;
      }
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
  
  public static boolean isBlock(ItemStack o) {
    return Block.getBlockById(Item.getIdFromItem(o.getItem())) != null;
  }
  
  public static boolean isItem(ItemStack o) {
    return !isBlock(o);
  }
  
  public static boolean isValidTool(ItemStack o) {
    return o.getItem().isItemTool(o);
  }
  
  public static boolean equalsSize(ItemStack itemstack1, ItemStack itemstack2) {
    if (itemstack1 == null || itemstack2 == null) {
      return false;
    }
    boolean main = equals(itemstack1, itemstack2);
    boolean size = itemstack1.stackSize == itemstack2.stackSize;
    if (main && size) {
      return true;
    }
    return false;
  }
  
  public static boolean equals(ItemStack itemstack1, ItemStack itemstack2) {
    if (itemstack1 == null || itemstack2 == null) {
      return false;
    }
    boolean id = itemstack1.getUnlocalizedName() == itemstack2.getUnlocalizedName();
    boolean meta = itemstack1.getItemDamage() == itemstack2.getItemDamage();
    boolean nbt = equalsNBT(itemstack1, itemstack2);
    if (id && meta && nbt) {
      return true;
    }
    return false;
  }
  
  public static boolean equalsIgnoreNBT(ItemStack itemstack1, ItemStack itemstack2) {
    if (itemstack1 == null || itemstack2 == null) {
      return false;
    }
    boolean id = itemstack1.getUnlocalizedName() == itemstack2.getUnlocalizedName();
    boolean meta = itemstack1.getItemDamage() == itemstack2.getItemDamage();
    if (id && meta) {
      return true;
    }
    return false;
  }
  
  public static boolean equalsNBT(ItemStack itemstack1, ItemStack itemstack2) {
    if (itemstack1 == null || itemstack2 == null) {
      return false;
    }
    boolean noNBT1 = itemstack1.stackTagCompound == null;
    boolean noNBT2 = itemstack2.stackTagCompound == null;
    if (noNBT1 && noNBT2) {
      return true;
    } else if ((!noNBT1 && noNBT2) || (noNBT1 && !noNBT2)) {
      return false;
    } else {
      return itemstack1.stackTagCompound.equals(itemstack2.stackTagCompound);
    }
  }
  
  public static boolean oreDictionary(ItemStack itemstack1, ItemStack itemstack2) {
    if (itemstack1 == null || itemstack2 == null) {
      return false;
    }
    if ((OreDictionary.getOreID(itemstack1) == OreDictionary.getOreID(itemstack2)) && OreDictionary.getOreID(itemstack1) != -1) {
      return true;
    }
    return false;
  }
}
