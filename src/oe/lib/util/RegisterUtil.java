package oe.lib.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegisterUtil {
  
  public static void Item(Item Item) {
    GameRegistry.registerItem(Item, Item.getUnlocalizedName());
  }
  
  public static void Block(Block Block, String Tool, int HarvestLevel) {
    GameRegistry.registerBlock(Block, Block.getUnlocalizedName());
    MinecraftForge.setBlockHarvestLevel(Block, Tool, HarvestLevel);
  }
  
  public static void Block(Block Block) {
    GameRegistry.registerBlock(Block, Block.getUnlocalizedName());
  }
  
  public static void Ore(String OreName, Block block) {
    OreDictionary.registerOre(OreName, block);
  }
  
  public static void Ore(String OreName, Item item) {
    OreDictionary.registerOre(OreName, item);
  }
}
