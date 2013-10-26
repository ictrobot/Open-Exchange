package oe.lib;

import oe.block.Blocks;
import oe.item.Items;
import oe.lib.helper.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingRecipes {
  public static void load() {
    ConfigHelper.load();
    // Items
    if (ConfigHelper.other("item", "buildRingEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Items.buildRing), "o o", " e ", "o o", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    }
    if (ConfigHelper.other("item", "readerEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Items.reader), "   ", "eoe", "   ", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    }
    // Blocks
    if (ConfigHelper.other("block", "condenserEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.condenser), "ooo", "eee", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    }
    if (ConfigHelper.other("block", "chargingEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.charging), "ooo", "oeo", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    }
    if (ConfigHelper.other("block", "extractorEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.extractor), "ooo", "eoe", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    }
    if (ConfigHelper.other("block", "storageEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.storage), "eee", "eee", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    }
    if (ConfigHelper.other("block", "transferEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.transfer), "eoe", "eoe", "eoe", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    }
    if (ConfigHelper.other("block", "experienceConsumerEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(Blocks.experienceConsumer), " e ", "eoe", " e ", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    }
    ConfigHelper.save();
    
  }
}
