package oe.lib;

import oe.block.Blocks;
import oe.item.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingRecipes {
  public static void load() {
    // Blocks
    GameRegistry.addRecipe(new ItemStack(Blocks.condenser), "ooo", "eee", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    GameRegistry.addRecipe(new ItemStack(Blocks.charging), "ooo", "oeo", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    GameRegistry.addRecipe(new ItemStack(Blocks.extractor), "ooo", "eoe", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    GameRegistry.addRecipe(new ItemStack(Blocks.storage), "eee", "eee", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    GameRegistry.addRecipe(new ItemStack(Blocks.transfer), "eoe", "eoe", "eoe", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    GameRegistry.addRecipe(new ItemStack(Blocks.experienceConsumer), " e ", "eoe", " e ", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    // Items
    GameRegistry.addRecipe(new ItemStack(Items.reader), "   ", "eoe", "   ", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    GameRegistry.addRecipe(new ItemStack(Items.buildRing), "o o", " e ", "o o", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
  }
}
