package oe.item;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;
import oe.lib.helper.ConfigHelper;
import oe.lib.helper.Register;
import oe.qmc.QMC;

public class Items {
  
  public static Item buildRing;
  public static Item reader;
  public static Item pickaxe;
  public static Item shovel;
  public static Item axe;
  
  public static EnumToolMaterial quantum = EnumHelper.addToolMaterial("quantum", 3, 0, 8.0F, 3.0F, 10);
  
  public static void Load() {
    ConfigHelper.load();
    if (ConfigHelper.other("item", "buildRingEnabled", true)) {
      buildRing = new ItemBuildRing(ItemIDs.buildRing);
    }
    if (ConfigHelper.other("item", "readerEnabled", true)) {
      reader = new ItemReader(ItemIDs.reader);
    }
    if (ConfigHelper.other("item", "toolsEnabled", true)) {
      pickaxe = new ItemQuantumPickaxe(ItemIDs.pickaxe);
      axe = new ItemQuantumAxe(ItemIDs.axe);
      shovel = new ItemQuantumShovel(ItemIDs.shovel);
    }
    ConfigHelper.save();
  }
  
  public static void Register() {
    ConfigHelper.load();
    if (ConfigHelper.other("item", "buildRingEnabled", true)) {
      Register.Item(buildRing, "Builder's Ring");
    }
    if (ConfigHelper.other("item", "readerEnabled", true)) {
      Register.Item(reader, QMC.name + " Reader");
    }
    if (ConfigHelper.other("item", "toolsEnabled", true)) {
      Register.Item(pickaxe, "Quantum Pickaxe");
      Register.Item(axe, "Quantum Axe");
      Register.Item(shovel, "Quantum Shovel");
    }
  }
  
  public static String Texture(String str) {
    return "OE:item" + str;
  }
}
