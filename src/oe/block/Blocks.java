package oe.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oe.helper.Register;
import oe.qmc.QMC;

public class Blocks {
  
  public static Block condenser;
  public static Block charging;
  public static Block extractor;
  public static Block storage;
  public static Block transfer;
  public static Block experienceConsumer;
  
  public static void Load() {
    condenser = new BlockCondenser(BlockIDs.condenserID);
    charging = new BlockCharging(BlockIDs.chargingID);
    extractor = new BlockExtractor(BlockIDs.extractorID);
    storage = new BlockStorage(BlockIDs.storageID);
    transfer = new BlockTransfer(BlockIDs.transferID, 3);
    experienceConsumer = new BlockExperienceConsumer(BlockIDs.experienceConsumerID);
  }
  
  public static void Register() {
    Register.Block(condenser, QMC.name + " Condenser", "pickaxe", 2);
    Register.Block(charging, QMC.name + " Charging Bench", "pickaxe", 2);
    Register.Block(extractor, QMC.name + " Extractor", "pickaxe", 2);
    Register.Block(storage, QMC.name + " Storage", "pickaxe", 2);
    Register.Block(transfer, QMC.name + " Transfer", "pickaxe", 2);
    Register.Block(experienceConsumer, QMC.name + " Experienced Consumer", "pickaxe", 2);
    
    GameRegistry.addRecipe(new ItemStack(condenser), "ooo", "eee", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    GameRegistry.addRecipe(new ItemStack(charging), "ooo", "oeo", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    GameRegistry.addRecipe(new ItemStack(extractor), "ooo", "eoe", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    GameRegistry.addRecipe(new ItemStack(storage), "eee", "eee", "ooo", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    GameRegistry.addRecipe(new ItemStack(transfer), "eoe", "eoe", "eoe", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
    GameRegistry.addRecipe(new ItemStack(experienceConsumer), " e ", "eoe", " e ", 'o', new ItemStack(Block.obsidian), 'e', new ItemStack(Item.enderPearl));
  }
  
  public static String Texture(String str) {
    return "OE:block" + str;
  }
}
