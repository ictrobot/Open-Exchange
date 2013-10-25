package oe.item;

import net.minecraft.item.Item;
import oe.lib.helper.Register;
import oe.qmc.QMC;

public class Items {
  
  public static Item buildRing;
  public static Item reader;
  
  public static void Load() {
    buildRing = new ItemBuildRing(ItemIDs.buildRingID);
    reader = new ItemReader(ItemIDs.readerID);
  }
  
  public static void Register() {
    Register.Item(buildRing, "Builder's Ring");
    Register.Item(reader, QMC.name + " Reader");
  }
  
  public static String Texture(String str) {
    return "OE:item" + str;
  }
}
