package oe.qmc;

import oe.helper.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class DefaultOreQMC extends QMC {
  public static void load() {
    ConfigHelper.load();
    add("logWood", QMC.getQMC(Block.wood));
    add("plankWood", QMC.getQMC(Block.planks));
    add("stickWood", QMC.getQMC(Item.stick));
    add("blockCobble", QMC.getQMC(Block.cobblestone));
    add("blockStone", QMC.getQMC(Block.stone));
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
