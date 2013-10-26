package oe.item;

import net.minecraft.item.Item;
import oe.lib.helper.ConfigHelper;
import oe.lib.helper.Register;
import oe.qmc.QMC;

public class Items {
  
  public static Item buildRing;
  public static Item reader;
  
  public static void Load() {
    ConfigHelper.load();
    if (ConfigHelper.other("item", "buildRingEnabled", true)) {
      buildRing = new ItemBuildRing(ItemIDs.buildRingID);
    }
    if (ConfigHelper.other("item", "readerEnabled", true)) {
      reader = new ItemReader(ItemIDs.readerID);
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
  }
  
  public static String Texture(String str) {
    return "OE:item" + str;
  }
}
