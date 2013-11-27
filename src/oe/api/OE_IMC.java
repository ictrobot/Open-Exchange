package oe.api;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * interact with OE by InterModComms
 */
public class OE_IMC {
  
  public static void addQMC(ItemStack o, double value) {
    NBTTagCompound nbt = new NBTTagCompound();
    NBTTagCompound item = new NBTTagCompound();
    o.writeToNBT(item);
    nbt.setCompoundTag("item", item);
    nbt.setDouble("qmc", value);
    FMLInterModComms.sendMessage("OE", "addQMCItemStack", nbt);
  }
  
  public static void addQMC(String ore, double value) {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setString("ore", ore);
    nbt.setDouble("qmc", value);
    FMLInterModComms.sendMessage("OE", "addQMCItemStack", nbt);
  }
  
  public static void addToolBlacklist(int BlockID) {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setInteger("blockID", BlockID);
    FMLInterModComms.sendMessage("OE", "addQuantumToolBlackList", nbt);
  }
}
