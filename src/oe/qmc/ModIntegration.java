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
    setMod("IC2");
    add("itemRubber", 24); // Rubber
    add("blockRubSapling", 8); // Rubber Tree Sapling
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
  
  static void setMod(String Mod) {
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
