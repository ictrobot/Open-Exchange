package oe.core.util;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class RegisterUtil {

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.FIELD)
  public @interface Register {
    Class<?> value();

    boolean enabled() default true;
  }

  public static void Item(Item item) {
    GameRegistry.registerItem(item, item.getUnlocalizedName());
  }
  
  /*
   * public static void Block(Block block, String tool, int harvestLevel) {
   * GameRegistry.registerBlock(block, block.getUnlocalizedName());
   * MinecraftForge.setBlockHarvestLevel(block, tool, harvestLevel);
   * }
   */

  public static void Block(Block block) {
    GameRegistry.registerBlock(block, block.getUnlocalizedName());
  }

  public static void Ore(String oreName, Block block) {
    OreDictionary.registerOre(oreName, block);
  }

  public static void Ore(String oreName, Item item) {
    OreDictionary.registerOre(oreName, item);
  }
}
