package oe.lib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oe.block.Blocks;
import oe.item.Items;
import oe.lib.helper.ConfigHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingRecipes {
  public static void load() {
    ConfigHelper.load();
    // Items
    if (ConfigHelper.other("item", "buildRingEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Items.buildRing), "o o", " e ", "o o", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.ingotIron));
    }
    if (ConfigHelper.other("item", "readerEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Items.reader), "   ", "eoe", "   ", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.ingotIron));
    }
    if (ConfigHelper.other("item", "toolsEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Items.pickaxe), "ttt", " s ", " s ", 't', new ItemStack(Block.obsidian), 's', new ItemStack(Item.ingotIron));
      GameRegistry.addRecipe(new ItemStack(Items.axe), "tt ", "ts ", " s ", 't', new ItemStack(Block.obsidian), 's', new ItemStack(Item.ingotIron));
      GameRegistry.addRecipe(new ItemStack(Items.shovel), " t ", " s ", " s ", 't', new ItemStack(Block.obsidian), 's', new ItemStack(Item.ingotIron));
    }
    if (ConfigHelper.other("item", "transmutationEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Items.transmutation), "sss", "sts", "sss", 't', new ItemStack(Block.obsidian), 's', new ItemStack(Item.ingotIron));
    }
    if (ConfigHelper.other("item", "repairEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Items.repair), "ttt", "sss", "sss", 't', new ItemStack(Block.obsidian), 's', new ItemStack(Item.ingotIron));
    }
    if (ConfigHelper.other("item", "blockManipulatorEnabled", true)) {
      GameRegistry.addShapelessRecipe(new ItemStack(Items.blockMover), new ItemStack(Block.obsidian), new ItemStack(Item.ingotIron));
    }
    // Blocks
    if (ConfigHelper.other("block", "condenserEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.condenser), "ooo", "eee", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.ingotGold));
    }
    if (ConfigHelper.other("block", "chargingEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.charging), "ooo", "oeo", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.ingotGold));
    }
    if (ConfigHelper.other("block", "extractorEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.extractor), "ooo", "eoe", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.ingotGold));
    }
    if (ConfigHelper.other("block", "storageEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.storage), "eee", "eee", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.ingotGold));
    }
    if (ConfigHelper.other("block", "experienceConsumerEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.experienceConsumer), " e ", "eoe", " e ", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.ingotGold));
    }
    if (ConfigHelper.other("block", "pipeEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.pipe, 16), "eee", "ooo", "eee", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.ingotGold));
    }
    if (ConfigHelper.other("block", "drillEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.drill), "oro", "rdr", "oro", 'o', new ItemStack(Block.obsidian), 'r', new ItemStack(Block.blockRedstone), 'd', new ItemStack(Item.diamond));
    }
    if (ConfigHelper.other("block", "drillRemoteEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.drillRemote), "bdb", "dbd", "bdb", 'b', new ItemStack(Block.blockDiamond), 'd', new ItemStack(Blocks.drill));
    }
    ConfigHelper.save();
    
  }
}
