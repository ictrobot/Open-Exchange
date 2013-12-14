package oe.core.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryUtil {
  
  public static void minecraftInit() {
    RegisterUtil.Ore("oreCoal", Block.oreCoal);
    RegisterUtil.Ore("ingotGold", Item.ingotGold);
    RegisterUtil.Ore("ingotIron", Item.ingotIron);
    RegisterUtil.Ore("materialCoal", Item.coal);
    RegisterUtil.Ore("gemDiamond", Item.diamond);
    RegisterUtil.Ore("materialRedstone", Item.redstone);
    RegisterUtil.Ore("gemEmerald", Item.emerald);
    RegisterUtil.Ore("materialNetherQuartz", Item.netherQuartz);
  }
  
  public static List<ItemStack> getItemStacks(String ore) {
    return OreDictionary.getOres(ore);
  }
  
  public static ItemStack getItemStack(String ore) {
    return ItemStackUtil.first(getItemStacks(ore));
  }
  
  public static String ore(ItemStack stack) {
    return ore(oreID(stack));
  }
  
  public static String ore(int id) {
    return OreDictionary.getOreName(id);
  }
  
  public static int oreID(ItemStack stack) {
    return OreDictionary.getOreID(stack);
  }
  
  public static int oreID(String ore) {
    return OreDictionary.getOreID(ore);
  }
  
  public static boolean isOre(ItemStack stack) {
    return oreID(stack) != -1;
  }
  
  public static List<String> getOreDictionaryStartingWith(String str, boolean trim) {
    String[] ores = OreDictionary.getOreNames();
    List<String> data = new ArrayList<String>();
    for (String ore : ores) {
      if (ore.startsWith(str)) {
        String toAdd = ore;
        if (trim) {
          toAdd = toAdd.substring(str.length());
        }
        data.add(toAdd);
      }
    }
    if (data.size() == 0) {
      return null;
    }
    return data;
  }
}
