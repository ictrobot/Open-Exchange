package oe.core;

import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import oe.core.util.ConfigUtil;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingRecipes {
  public static void load() {
    // Items
    if (ConfigUtil.other("item", "buildRingEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(oe.item.OEItems.buildRing), "o o", " e ", "o o", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.iron_ingot));
    }
    if (ConfigUtil.other("item", "readerEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(oe.item.OEItems.info), "   ", "eoe", "   ", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.iron_ingot));
    }
    if (ConfigUtil.other("item", "toolsEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(oe.item.OEItems.pickaxe), "ttt", " s ", " s ", 't', new ItemStack(Blocks.obsidian), 's', new ItemStack(Items.iron_ingot));
      GameRegistry.addRecipe(new ItemStack(oe.item.OEItems.axe), "tt ", "ts ", " s ", 't', new ItemStack(Blocks.obsidian), 's', new ItemStack(Items.iron_ingot));
      GameRegistry.addRecipe(new ItemStack(oe.item.OEItems.shovel), " t ", " s ", " s ", 't', new ItemStack(Blocks.obsidian), 's', new ItemStack(Items.iron_ingot));
    }
    if (ConfigUtil.other("item", "repairEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(oe.item.OEItems.repair), "ttt", "sss", "sss", 't', new ItemStack(Blocks.obsidian), 's', new ItemStack(Items.iron_ingot));
    }
    if (ConfigUtil.other("item", "blockManipulatorEnabled", true)) {
      GameRegistry.addShapelessRecipe(new ItemStack(oe.item.OEItems.blockManipulator), new ItemStack(Blocks.obsidian), new ItemStack(Items.iron_ingot));
    }
    // Blocks
    if (ConfigUtil.other("block", "condenserEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(oe.block.OEBlocks.condenser), "ooo", "eee", "ooo", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.gold_ingot));
    }
    if (ConfigUtil.other("block", "chargingEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(oe.block.OEBlocks.charging), "ooo", "oeo", "ooo", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.gold_ingot));
    }
    if (ConfigUtil.other("block", "extractorEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(oe.block.OEBlocks.extractor), "ooo", "eoe", "ooo", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.gold_ingot));
    }
    if (ConfigUtil.other("block", "storageEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(oe.block.OEBlocks.storage), "eee", "eee", "ooo", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.gold_ingot));
    }
    if (ConfigUtil.other("block", "experienceConsumerEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(oe.block.OEBlocks.experienceConsumer), " e ", "eoe", " e ", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.gold_ingot));
    }
    if (ConfigUtil.other("block", "pipeEnabled", true)) {
      GameRegistry.addRecipe(new ItemStack(oe.block.OEBlocks.pipe, 16), "eee", "ooo", "eee", 'o', new ItemStack(Blocks.obsidian), 'e', new ItemStack(Items.gold_ingot));
    }
  }
}
