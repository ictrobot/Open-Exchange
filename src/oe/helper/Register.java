package oe.helper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Register {
  
  public static void Item(Item Item, String Name) {
    LanguageRegistry.addName(Item, Name);
    GameRegistry.registerItem(Item, Item.getUnlocalizedName());
  }
  
  public static void Block(Block Block, String Name, String Tool, int HarvestLevel) {
    LanguageRegistry.addName(Block, Name);
    GameRegistry.registerBlock(Block, Block.getUnlocalizedName());
    MinecraftForge.setBlockHarvestLevel(Block, Tool, HarvestLevel);
  }
  
  public static void Block(Block Block, String Name) {
    LanguageRegistry.addName(Block, Name);
    GameRegistry.registerBlock(Block, Block.getUnlocalizedName());
  }
  
  public static void Ore(String OreName, Block block) {
    OreDictionary.registerOre(OreName, block);
  }
  
  public static void Ore(String OreName, Item item) {
    OreDictionary.registerOre(OreName, item);
  }
}
