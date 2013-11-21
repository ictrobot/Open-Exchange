package oe.qmc.guess;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.OEGuesser;
import oe.lib.Log;
import oe.lib.helper.OreDictionaryHelper;
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
  
  public static class Ores {
    public String full;
    public String ore;
    
    public Ores(String Full, String Ore) {
      this.full = Full; // Example: ingotCopper
      this.ore = Ore; // Example: Copper
    }
  }
  
  private static Ores[] ores = new Ores[0];
  private static Prefix[] prefixes = new Prefix[] { new Prefix("dustTiny", (1 / 9)), new Prefix("dust", 1), new Prefix("plate", 1), new Prefix("itemCasing", 0.5), new Prefix("block", 9), new Prefix("nugget", (1 / 9)) };
  
  public static void init() {
    Log.debug("Loading Ore Guesser");
    for (String ore : OreDictionaryHelper.getOreDictionaryStartingWith("ingot", true)) {
      addOre("ingot" + ore, ore);
    }
    for (String ore : OreDictionaryHelper.getOreDictionaryStartingWith("gem", true)) {
      addOre("gem" + ore, ore);
    }
    for (String ore : OreDictionaryHelper.getOreDictionaryStartingWith("material", true)) {
      addOre("material" + ore, ore);
    }
  }
  
  public static void addOre(String full, String ore) {
    Ores[] tmp = new Ores[ores.length + 1];
    System.arraycopy(ores, 0, tmp, 0, ores.length);
    ores = tmp;
    ores[ores.length - 1] = new Ores(full, ore);
  }
  
  public static Guess.Data check(ItemStack itemstack) {
    int oreID = OreDictionary.getOreID(itemstack);
    if (oreID != -1) {
      String ore = OreDictionary.getOreName(OreDictionary.getOreID(itemstack));
      Ores o = null;
      Prefix prefix = null;
      for (Ores o_ : ores) {
        for (Prefix prefix_ : prefixes) {
          String combo = prefix_.prefix + o_.ore;
          if (ore.toLowerCase().matches(combo.toLowerCase())) {
            o = o_;
            prefix = prefix_;
            break;
          }
        }
        if (o != null) {
          break;
        }
      }
      if (o != null) {
        double value = QMC.getQMC(o.full);
        if (value > 0) {
          ItemStack[] src = new ItemStack[] { OreDictionaryHelper.getItemStacks(o.full)[0] };
          Guess.Data guess = new Guess.Data(src, value * prefix.ingotsNum, (int) prefix.ingotsNum);
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
