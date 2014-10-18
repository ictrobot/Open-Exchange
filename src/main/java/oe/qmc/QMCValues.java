package oe.qmc;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class QMCValues {
  public static void load() {

    add(Blocks.cobblestone, 1);
    add(Blocks.stone, 1);
    add(Blocks.dirt, 1);
    add(Blocks.gravel, 1);
    add(Blocks.grass, 1);
    add(Blocks.mycelium, 8);
    add(Blocks.sand, 1);
    add(Items.diamond, 8192);
    add(Items.gold_ingot, 2048);
    add(Items.iron_ingot, 256);
    add(Blocks.sapling, 8);
    add(Blocks.leaves, 1);
    add(Blocks.log, 32);
    add(Blocks.red_flower, 16);
    add(Blocks.yellow_flower, 16);
    add(Blocks.glowstone, 256);
    add(Items.slime_ball, 64);
    add(Items.clay_ball, 32);
    add(Blocks.web, 8);
    add(Blocks.tallgrass, 8);
    add(Blocks.deadbush, 8);
    add(Blocks.red_mushroom_block, 4);
    add(Blocks.brown_mushroom_block, 4);
    add(Blocks.wool, 48);
    add(Items.string, 12);
    add(Items.gunpowder, 256);
    add(Items.coal, 128); // Coal
    add(Blocks.mossy_cobblestone, 128);
    add(Items.leather, 12);
    // add(Blocks.reeds, 16);
    add(Items.reeds, 16);
    add(Items.wheat, 24);
    add(Items.carrot, 24);
    add(Items.potato, 24);
    add(Items.poisonous_potato, 12);
    add(Items.baked_potato, 24);
    add(Blocks.carrots, 12);
    add(Blocks.potatoes, 12);
    add(Blocks.obsidian, 128);
    add(Items.redstone, 64);
    add(Items.snowball, 1);
    add(Blocks.ice, 16);
    add(Blocks.cactus, 8);
    add(Items.clay_ball, 8);
    add(Blocks.pumpkin, 64);
    add(Items.pumpkin_seeds, 16);
    add(Items.melon, 12);
    add(Blocks.melon_block, 108);
    add(Blocks.netherrack, 1);
    add(Blocks.soul_sand, 4);
    add(Items.glowstone_dust, 64);
    add(Items.egg, 32);
    add(Blocks.vine, 16);
    add(Blocks.waterlily, 16);
    add(Blocks.nether_brick, 4);
    // add(Blocks.nether_wart, 32);
    add(Blocks.end_stone, 8);
    add(Items.ender_pearl, 1024);
    add(Items.blaze_rod, 512);
    add(Items.emerald, 16384);
    add(Items.quartz, 256);
    add(Blocks.cocoa, 24);
    add(Items.flint, 4);
    add(Items.feather, 12);
    add(Items.chicken, 12);
    add(Items.feather, 12);
    add(Items.beef, 32);
    add(Items.porkchop, 32);
    add(Items.bone, 12);
    add(Items.rotten_flesh, 12);
    add(Items.ghast_tear, 2048);
    add(Items.spider_eye, 12);
    add(Items.fish, 32);
    add(Items.wheat_seeds, 24);

    int[] dyeValues = new int[]{8, 8, 8, 8, 768, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};
    for (int i = 0; i < dyeValues.length; i++) {
      ItemStack stack = new ItemStack(Items.dye, 1, i);
      add(stack, dyeValues[i]);
    }

    add(FluidRegistry.LAVA, 16);
    add(FluidRegistry.WATER, 1);
    add(Items.milk_bucket, 784); // Because milk is not a fluid

    add(Items.record_13, 16384);
    add(Items.record_cat, 16384);
    add(Items.record_blocks, 16384);
    add(Items.record_chirp, 16384);
    add(Items.record_far, 16384);
    add(Items.record_mall, 16384);
    add(Items.record_mellohi, 16384);
    add(Items.record_stal, 16384);
    add(Items.record_strad, 16384);
    add(Items.record_ward, 16384);
    add(Items.record_11, 16384);
    add(Items.record_wait, 16384);
  }

  private static void add(Block block, double qmc) {
    add(new ItemStack(block), qmc);
  }

  private static void add(Item item, double qmc) {
    add(new ItemStack(item), qmc);
  }

  private static void add(Fluid fluid, double qmc) {
    QMC.add(fluid, qmc);
  }

  private static void add(ItemStack itemstack, double qmc) {
    QMC.add(itemstack, qmc);
  }
}
