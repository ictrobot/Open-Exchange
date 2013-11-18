package oe.lib.helper;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryHelper {
  
  public static void minecraftInit() {
    Register.Ore("oreCoal", Block.oreCoal);
    Register.Ore("ingotGold", Item.ingotGold);
    Register.Ore("ingotIron", Item.ingotIron);
    Register.Ore("materialCoal", Item.coal);
    Register.Ore("gemDiamond", Item.diamond);
    Register.Ore("materialRedstone", Item.redstone);
    Register.Ore("gemEmerald", Item.emerald);
    Register.Ore("materialNetherQuartz", Item.netherQuartz);
  }
  
  public static ItemStack[] getItemStacks(String ore) {
    ArrayList<ItemStack> stacks = OreDictionary.getOres(ore);
    ItemStack[] toReturn = new ItemStack[0];
    for (Object o : stacks) {
      if (o instanceof ItemStack) {
        ItemStack[] tmp = new ItemStack[toReturn.length + 1];
        System.arraycopy(toReturn, 0, tmp, 0, toReturn.length);
        toReturn = tmp;
        toReturn[toReturn.length - 1] = (ItemStack) o;
      }
    }
    if (toReturn.length == 0) {
      return null;
    }
    return toReturn;
  }
  
  public static String[] getOreDictionaryStartingWith(String str, boolean trim) {
    String[] ores = OreDictionary.getOreNames();
    String[] toReturn = new String[0];
    for (String ore : ores) {
      if (ore.startsWith(str)) {
        String toAdd = ore;
        if (trim) {
          toAdd = toAdd.substring(str.length());
        }
        String[] tmp = new String[toReturn.length + 1];
        System.arraycopy(toReturn, 0, tmp, 0, toReturn.length);
        toReturn = tmp;
        toReturn[toReturn.length - 1] = toAdd;
      }
    }
    if (toReturn.length == 0) {
      return null;
    }
    return toReturn;
  }
}
