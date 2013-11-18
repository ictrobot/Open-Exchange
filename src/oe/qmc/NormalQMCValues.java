package oe.qmc;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oe.lib.Log;

public class NormalQMCValues extends QMC {
  public static void load() {
    
    int r = length();
    // Minecraft
    add(Block.cobblestone, 1);
    add(Block.stone, 1);
    add(Block.dirt, 1);
    add(Block.gravel, 1);
    add(Block.grass, 1);
    add(Block.mycelium, 8);
    add(Block.sand, 1);
    add(Item.diamond, 8192);
    add(Item.ingotGold, 2048);
    add(Item.ingotIron, 256);
    add(Block.sapling, 8);
    add(Block.leaves, 1);
    add(Block.wood, 32);
    add(Block.plantRed, 16);
    add(Block.plantYellow, 16);
    add(Item.glowstone, 256);
    add(Item.slimeBall, 64);
    add(Item.clay, 32);
    add(Block.web, 8);
    add(Block.tallGrass, 8);
    add(Block.deadBush, 8);
    add(Block.mushroomBrown, 4);
    add(Block.mushroomRed, 4);
    add(Block.cloth, 48); // Wool
    add(Item.silk, 12); // String
    add(Item.gunpowder, 256);
    add(Item.coal, 128); // Coal
    add(Block.cobblestoneMossy, 128);
    add(Item.leather, 12);
    add(Block.reed, 16);
    add(Item.reed, 16);
    add(Item.wheat, 24);
    add(Item.carrot, 24);
    add(Item.potato, 24);
    add(Item.poisonousPotato, 12);
    add(Item.bakedPotato, 24);
    add(Block.carrot, 12);
    add(Block.potato, 12);
    add(Block.obsidian, 128);
    add(Item.redstone, 64);
    add(Item.snowball, 1);
    add(Block.ice, 16);
    add(Block.cactus, 8);
    add(Item.clay, 8);
    add(Block.pumpkin, 64);
    add(Item.pumpkinSeeds, 16);
    add(Item.melon, 12);
    add(Block.melon, 108);
    add(Block.netherrack, 1);
    add(Block.slowSand, 4);
    add(Item.glowstone, 64);
    add(Item.egg, 32);
    add(Block.vine, 16);
    add(Block.waterlily, 16);
    add(Block.netherBrick, 4);
    add(Block.netherStalk, 32);
    add(Block.whiteStone, 8);
    add(Item.enderPearl, 1024);
    add(Item.blazeRod, 512);
    add(Item.emerald, 16384);
    add(Item.netherQuartz, 512);
    add(Block.cocoaPlant, 24);
    add(Item.flint, 1);
    add(Item.feather, 12);
    add(Item.chickenRaw, 12);
    add(Item.feather, 12);
    add(Item.beefRaw, 32);
    add(Item.porkRaw, 32);
    add(Item.bone, 12);
    add(Item.rottenFlesh, 12);
    add(Item.ghastTear, 2048);
    add(Item.spiderEye, 12);
    add(Item.fishRaw, 32);
    add(Item.seeds, 24);
    
    int[] dyeValues = new int[] { 8, 8, 8, 8, 768, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 };
    for (int i = 0; i < dyeValues.length; i++) {
      ItemStack stack = new ItemStack(351, 1, i);
      add(stack, dyeValues[i]);
    }
    
    // Values required for OreDictionary before guessing
    add(Block.planks, getQMC(Block.wood) / 4);
    add(Item.stick, getQMC(Block.planks) / 2);
    
    // Other Values that are not from crafting/smelting etc;
    add(Item.bucketMilk, (getQMC(Item.ingotIron) * 3) + 32);
    add(Item.bucketWater, (getQMC(Item.ingotIron) * 3) + 2);
    add(Item.bucketLava, (getQMC(Item.ingotIron) * 3) + 16);
    add(Item.netherStar, getQMC(Item.diamond) * 64);
    
    add(Item.record13, 16384);
    add(Item.recordCat, getQMC(Item.record11));
    add(Item.recordBlocks, getQMC(Item.record11));
    add(Item.recordChirp, getQMC(Item.record11));
    add(Item.recordFar, getQMC(Item.record11));
    add(Item.recordMall, getQMC(Item.record11));
    add(Item.recordMellohi, getQMC(Item.record11));
    add(Item.recordStal, getQMC(Item.record11));
    add(Item.recordStrad, getQMC(Item.record11));
    add(Item.recordWard, getQMC(Item.record11));
    add(Item.record11, getQMC(Item.record11));
    add(Item.recordWait, getQMC(Item.record11));
    int reg = length() - r;
    Log.info(reg + " Minecraft " + nameFull + " Values Loaded");
  }
}
