package oe.qmc;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModIntegration {
  
  public static class Data {
    public String mod;
    public String name;
    public double value;
    public int meta;
    
    public Data(String Mod, String Name, int Meta, double Value) {
      this.mod = Mod;
      this.name = Name;
      this.meta = Meta;
      this.value = Value;
    }
  }
  
  private static Data[] data = new Data[0];
  
  public static void init() {
    mod("IC2");
    add("itemRubber", 24); // Rubber
    add("blockRubSapling", 8); // Rubber Tree Sapling
    mod("Thaumcraft");
    add("ItemShard", 0, 512); // Shards
    add("ItemShard", 1, 512);
    add("ItemShard", 2, 512);
    add("ItemShard", 3, 512);
    add("ItemShard", 4, 512);
    add("ItemShard", 5, 512);
    add("ItemWispEssence", 2048); // Wisp
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
    loop();
  }
  
  private static void loop() {
    for (Data d : data) {
      ItemStack stack = GameRegistry.findItemStack(d.mod, d.name, 1);
      if (stack != null) {
        stack.setItemDamage(d.meta);
        QMC.add(stack, d.value);
      }
    }
  }
  
  static String mod;
  
  static void mod(String Mod) {
    mod = Mod;
  }
  
  static void add(String Name, double Value) {
    add(Name, 0, Value);
  }
  
  static void add(String Name, int Meta, double Value) {
    Data[] tmp = new Data[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
    data[data.length - 1] = new Data(mod, Name, Meta, Value);
  }
}
