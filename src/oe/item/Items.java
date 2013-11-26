package oe.item;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;
import oe.lib.util.ConfigUtil;
import oe.lib.util.RegisterUtil;

public class Items {
  
  public static Item buildRing;
  public static Item info;
  public static Item pickaxe;
  public static Item shovel;
  public static Item axe;
  public static Item repair;
  public static Item blockManipulator;
  
  public static EnumToolMaterial quantum = EnumHelper.addToolMaterial("quantum", 3, 0, 8.0F, 3.0F, 10);
  
  public static void Load() {
    ConfigUtil.load();
    if (ConfigUtil.other("item", "buildRingEnabled", true)) {
      buildRing = new ItemBuildRing(ItemIDs.buildRing);
    }
    if (ConfigUtil.other("item", "infoEnabled", true)) {
      info = new ItemInfo(ItemIDs.reader);
    }
    if (ConfigUtil.other("item", "toolsEnabled", true)) {
      pickaxe = new ItemQuantumPickaxe(ItemIDs.pickaxe);
      axe = new ItemQuantumAxe(ItemIDs.axe);
      shovel = new ItemQuantumShovel(ItemIDs.shovel);
    }
    if (ConfigUtil.other("item", "repairEnabled", true)) {
      repair = new ItemRepair(ItemIDs.repair);
    }
    if (ConfigUtil.other("item", "blockManipulatorEnabled", true)) {
      blockManipulator = new ItemBlockManipulator(ItemIDs.blockMover);
    }
    ConfigUtil.save();
  }
  
  public static void Register() {
    ConfigUtil.load();
    if (ConfigUtil.other("item", "buildRingEnabled", true)) {
      RegisterUtil.Item(buildRing);
    }
    if (ConfigUtil.other("item", "infoEnabled", true)) {
      RegisterUtil.Item(info);
    }
    if (ConfigUtil.other("item", "toolsEnabled", true)) {
      RegisterUtil.Item(pickaxe);
      RegisterUtil.Item(axe);
      RegisterUtil.Item(shovel);
    }
    if (ConfigUtil.other("item", "repairEnabled", true)) {
      RegisterUtil.Item(repair);
    }
    if (ConfigUtil.other("item", "blockManipulatorEnabled", true)) {
      RegisterUtil.Item(blockManipulator);
    }
  }
  
  public static String Texture(String str) {
    return "OE:item" + str;
  }
}
