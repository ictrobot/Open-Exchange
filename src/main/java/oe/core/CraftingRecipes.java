package oe.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import oe.block.OEBlocks;
import oe.core.util.ConfigUtil;
import oe.item.OEItems;

public class CraftingRecipes {
  public static void load() {
    // TODO Redo
    // Items
    if (ConfigUtil.other("item", "buildRingEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(OEItems.buildRing), "o o", " e ", "o o", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.iron_ingot));
    }
    if (ConfigUtil.other("item", "readerEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(OEItems.info), "   ", "eoe", "   ", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.iron_ingot));
    }
    if (ConfigUtil.other("item", "toolsEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(OEItems.pickaxe), "ttt", " s ", " s ", 't', new ItemStack(Blocks.obsidian), 's', new ItemStack(Items.iron_ingot));
      GameRegistry.addRecipe(new ItemStack(OEItems.axe), "tt ", "ts ", " s ", 't', new ItemStack(Blocks.obsidian), 's', new ItemStack(Items.iron_ingot));
      GameRegistry.addRecipe(new ItemStack(OEItems.shovel), " t ", " s ", " s ", 't', new ItemStack(Blocks.obsidian), 's', new ItemStack(Items.iron_ingot));
    }
    if (ConfigUtil.other("item", "repairEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(OEItems.repair), "ttt", "sss", "sss", 't', new ItemStack(Blocks.obsidian), 's', new ItemStack(Items.iron_ingot));
    }
    if (ConfigUtil.other("item", "blockManipulatorEnabled", true)) {
      GameRegistry.addShapelessRecipe(new ItemStack(OEItems.blockManipulator), new ItemStack(Blocks.obsidian), new ItemStack(Items.iron_ingot));
    }
    // Blocks
    if (ConfigUtil.other("block", "condenserEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(OEBlocks.condenser), "ooo", "eee", "ooo", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.gold_ingot));
    }
    if (ConfigUtil.other("block", "chargingEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(OEBlocks.charging), "ooo", "oeo", "ooo", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.gold_ingot));
    }
    if (ConfigUtil.other("block", "extractorEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(OEBlocks.extractor), "ooo", "eoe", "ooo", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.gold_ingot));
    }
    if (ConfigUtil.other("block", "storageEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(OEBlocks.storage), "eee", "eee", "ooo", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.gold_ingot));
    }
    if (ConfigUtil.other("block", "experienceConsumerEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(OEBlocks.experienceConsumer), " e ", "eoe", " e ", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.gold_ingot));
    }
    if (ConfigUtil.other("block", "pipeEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(OEBlocks.pipe, 16), "eee", "ooo", "eee", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.gold_ingot));
    }
  }
}
