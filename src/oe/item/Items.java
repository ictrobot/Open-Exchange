package oe.item;

import net.minecraft.item.Item;
import oe.helper.Register;

public class Items {
  
  public static Item buildRing;
  
  public static void Load() {
    buildRing = new ItemBuildRing(ItemIDs.buildRingID);
  }
  
  public static void Register() {
    Register.Item(buildRing, "Builder's Ring");
  }
  
  public static String Texture(String str) {
    return "OE:item" + str;
  }
}
