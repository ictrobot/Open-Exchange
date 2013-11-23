package oe.qmc;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import oe.lib.util.FluidUtil;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModIntegration {
  
  public static class Data {
    public String mod;
    public String name;
    public double value;
    public boolean isOre;
    public boolean isLiquid;
    public int meta;
    
    public Data(String Mod, String Name, int Meta, double Value) {
      this.mod = Mod;
      this.name = Name;
      this.meta = Meta;
      this.value = Value;
      isOre = false;
    }
    
    public Data(String ore, double Value, boolean isFluid) {
      this.name = ore;
      this.value = Value;
      this.isOre = !isFluid;
      this.isLiquid = isFluid;
    }
  }
  
  private static Data[] data = new Data[0];
  
  public static void init() {
    mod("IC2");
    item("itemRubber", 0, 24); // Rubber
    item("blockRubSapling", 0, 8); // Rubber Tree Sapling
    item("itemCellEmpty", 0, 196 / 3);
    
    mod("Thaumcraft");
    item("ItemShard", 0, 512); // Shards
    item("ItemShard", 1, 512);
    item("ItemShard", 2, 512);
    item("ItemShard", 3, 512);
    item("ItemShard", 4, 512);
    item("ItemShard", 5, 512);
    item("ItemWispEssence", 0, 2048); // Wisp
    item("blockMagicalLog", 0, 512); // Thaumcraft wood
    item("blockMagicalLog", 1, 512);
    
    mod("Railcraft");
    item("tile.railcraft.brick.quarried", 2, 16); // Railcraft Stones
    item("tile.railcraft.brick.quarried", 5, 16);
    item("tile.railcraft.brick.bloodstained", 2, 16);
    item("tile.railcraft.brick.bloodstained", 5, 16);
    item("tile.railcraft.brick.frostbound", 2, 16);
    item("tile.railcraft.brick.frostbound", 5, 16);
    item("tile.railcraft.brick.nether", 2, 16);
    item("tile.railcraft.brick.nether", 5, 16);
    item("tile.railcraft.brick.abyssal", 2, 16);
    item("tile.railcraft.brick.abyssal", 5, 16);
    
    mod("AppliedEnergistics");
    item("AppEngMaterials", 7, 512); // Quartz Dust
    item("AppEngMaterials", 6, 512); // Quartz
    
    fluid("oil", 128);
    fluid("fuel", 192);
    
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
      } else if (d.isLiquid) {
        Fluid fluid = FluidUtil.getFluid(d.name);
        if (fluid != null) {
          QMC.add(fluid, d.value);
        }
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
  
  private static void mod(String Mod) {
    mod = Mod;
  }
  
  /**
   * Ore Dictionary
   * 
   * @param Name
   * @param Value
   */
  private static void ore(String Name, double Value) {
    Data[] tmp = new Data[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
    data[data.length - 1] = new Data(Name, Value, false);
  }
  
  /**
   * Mod Item
   * 
   * @param Name
   * @param Value
   */
  private static void item(String Name, int Meta, double Value) {
    Data[] tmp = new Data[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
    data[data.length - 1] = new Data(mod, Name, Meta, Value);
  }
  
  /**
   * Fluid
   * 
   * @param Name
   * @param Value
   */
  private static void fluid(String Name, double Value) {
    Data[] tmp = new Data[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
    data[data.length - 1] = new Data(Name, Value, true);
  }
}
