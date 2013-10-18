package oe.value;

import oe.helper.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class DefaultOre extends OreValues {
  public static void load() {
    ConfigHelper.load();
    add("logWood", Values.getValue(Block.wood));
    add("plankWood", Values.getValue(Block.wood));
    add("stickWood", Values.getValue(Item.stick));
    add("blockCobble", Values.getValue(Block.cobblestone));
    add("blockStone", Values.getValue(Block.stone));
    config("ingotCopper", 192);
    config("ingotTin", 192);
    config("ingotLead", 256);
    config("ingotSilver", 256);
    config("gemRuby", 2048);
    config("gemSapphire", 2048);
    config("gemGreenSapphire", 2048);
    ConfigHelper.save();
  }
  
  public static void config(String str, int normal) {
    add(str, ConfigHelper.other("Ore Dictionary Values", str, normal));
  }
}
