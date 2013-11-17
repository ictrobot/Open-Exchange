package oe.qmc;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModIntegration {
  
  public static class Data {
    public String mod;
    public String name;
    public double value;
    public boolean isOre;
    public int meta;
    
    public Data(String Mod, String Name, int Meta, double Value) {
      this.mod = Mod;
      this.name = Name;
      this.meta = Meta;
      this.value = Value;
      isOre = false;
    }
    
    public Data(String ore, double Value) {
      this.name = ore;
      this.value = Value;
      this.isOre = true;
    }
  }
  
  private static Data[] data = new Data[0];
  
  public static void init() {
    mod("IC2");
    add("itemRubber", 0, 24); // Rubber
    add("blockRubSapling", 0, 8); // Rubber Tree Sapling
    
    mod("Thaumcraft");
    add("ItemShard", 0, 512); // Shards
    add("ItemShard", 1, 512);
    add("ItemShard", 2, 512);
    add("ItemShard", 3, 512);
    add("ItemShard", 4, 512);
    add("ItemShard", 5, 512);
    add("ItemWispEssence", 0, 2048); // Wisp
    add("blockMagicalLog", 0, 512); // Thaumcraft wood
    add("blockMagicalLog", 1, 512);
    
    mod("Railcraft");
    add("tile.railcraft.brick.quarried", 2, 16); // Railcraft Stones
    add("tile.railcraft.brick.quarried", 5, 16);
    add("tile.railcraft.brick.bloodstained", 2, 16);
    add("tile.railcraft.brick.bloodstained", 5, 16);
    add("tile.railcraft.brick.frostbound", 2, 16);
    add("tile.railcraft.brick.frostbound", 5, 16);
    add("tile.railcraft.brick.nether", 2, 16);
    add("tile.railcraft.brick.nether", 5, 16);
    add("tile.railcraft.brick.abyssal", 2, 16);
    add("tile.railcraft.brick.abyssal", 5, 16);
    mod("AppliedEnergistics");
    add("AppEngMaterials", 7, 512); // Quartz Dust
    add("AppEngMaterials", 6, 512); // Quartz
    
    ore("ingotPlatinum", 4096); // Thermal Expansion 3
    ore("ingotElectrum", 1152);
    ore("ingotInvar", 512);
    ore("ingotEnderium", 13376);
    
    ore("ingotCopper", 192);
    ore("ingotTin", 192);
    ore("ingotBronze", 192);
    ore("ingotLead", 256);
    ore("ingotSilver", 256);
    ore("ingotNickel", 1024);
    ore("gemRuby", 2048);
    ore("gemSapphire", 2048);
    ore("gemGreenSapphire", 2048);
    
    loop();
  }
  
  private static void loop() {
    for (Data d : data) {
      if (d.isOre) {
        QMC.add(d.name, d.value);
      } else {
        ItemStack stack = GameRegistry.findItemStack(d.mod, d.name, 1);
        if (stack != null) {
          stack.setItemDamage(d.meta);
          QMC.add(stack, d.value);
        }
      }
    }
  }
  
  static String mod;
  
  static void mod(String Mod) {
    mod = Mod;
  }
  
  /**
   * Ore Dictionary
   * 
   * @param Name
   * @param Value
   */
  static void ore(String Name, double Value) {
    Data[] tmp = new Data[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
    data[data.length - 1] = new Data(Name, Value);
  }
  
  /**
   * Mod Item
   * 
   * @param Name
   * @param Value
   */
  static void add(String Name, int Meta, double Value) {
    Data[] tmp = new Data[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
    data[data.length - 1] = new Data(mod, Name, Meta, Value);
  }
}
