package oe.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.event.FMLInterModComms;

/**
 * interact with OE by InterModComms
 */
public class OE_IMC {
  
  public static void addQMC(ItemStack o, double value) {
    NBTTagCompound nbt = new NBTTagCompound();
    NBTTagCompound item = new NBTTagCompound();
    o.writeToNBT(item);
    nbt.setTag("item", item);
    nbt.setDouble("qmc", value);
    FMLInterModComms.sendMessage("OE", "addQMCItemStack", nbt);
  }
  
  public static void addQMC(String ore, double value) {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setString("ore", ore);
    nbt.setDouble("qmc", value);
    FMLInterModComms.sendMessage("OE", "addQMCItemStack", nbt);
  }
  
  public static void addToolBlacklist(Block block) {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setInteger("blockID", Block.getIdFromBlock(block));
    FMLInterModComms.sendMessage("OE", "addQuantumToolBlackList", nbt);
  }
}
