package oe.qmc.guess;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.OEGuesser;
import oe.lib.Log;
import oe.lib.handler.ore.OreData;
import oe.lib.handler.ore.OreDictionaryHelper;
import oe.qmc.QMC;

public class Ore extends OEGuesser {
  
  public static class Prefix {
    public double ingotsNum;
    public String prefix;
    
    public Prefix(String str, double ingots) {
      this.ingotsNum = ingots;
      this.prefix = str;
    }
  }
  
  private static String[] ores = new String[0];
  private static Prefix[] prefixes = new Prefix[] { new Prefix("dust", 1), new Prefix("block", 9), new Prefix("nugget", (1 / 9)) };
  
  public static void init() {
    Log.debug("Loading Ore Guesser");
    for (OreData ore : OreDictionaryHelper.oreDataArray()) {
      if (ore.ore.startsWith("ingot")) {
        String oreValue = ore.ore.substring(5);
        String[] tmp = new String[ores.length + 1];
        System.arraycopy(ores, 0, tmp, 0, ores.length);
        ores = tmp;
        ores[ores.length - 1] = oreValue;
      }
    }
  }
  
  public static GuessReturn check(ItemStack itemstack) {
    if (itemstack.itemID == 30066 && itemstack.getItemDamage() == 3) {
      Log.info(itemstack);
    }
    if (OreDictionary.getOreID(itemstack) != -1) {
      String ore = OreDictionary.getOreName(OreDictionary.getOreID(itemstack));
      String ingot2 = "";
      Prefix prefix2 = null;
      for (String ingot : ores) {
        for (Prefix prefix : prefixes) {
          String combo = prefix.prefix + ingot;
          if (ore.toLowerCase().matches(combo.toLowerCase())) {
            ingot2 = ingot;
            prefix2 = prefix;
            break;
          }
        }
        if (ingot2 != "") {
          break;
        }
      }
      if (ingot2 != "") {
        double ingotValue = QMC.getQMC(OreDictionaryHelper.getItemStacks("ingot" + ingot2)[0]);
        if (ingotValue > 0) {
          ItemStack[] ingot = new ItemStack[] { OreDictionaryHelper.getItemStacks("ingot" + ingot2)[0] };
          GuessReturn guess = new GuessReturn(ingot, new double[] { ingotValue }, ingotValue * prefix2.ingotsNum, (int) prefix2.ingotsNum);
          return guess;
        }
      }
    }
    return null;
  }
  
  public static int[] meta(int ID) {
    return new int[] {};
  }
}
