package oe.lib.handler;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import oe.lib.Log;
import oe.lib.misc.QuantumToolBlackList;
import oe.lib.util.ItemStackUtil;
import oe.qmc.QMC;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;

public class IMCHandler {
  
  public static String addQMCItemStack = "addQMCItemStack";
  public static String toolBlackList = "addQuantumToolBlackList";
  
  public static void processIMCMessages(IMCEvent event) {
    for (IMCMessage imcMessage : event.getMessages()) {
      if (imcMessage.getMessageType() == NBTTagCompound.class) {
        if (imcMessage.key.equalsIgnoreCase(addQMCItemStack)) {
          addQMCItemStack(imcMessage);
        } else if (imcMessage.key.equalsIgnoreCase(toolBlackList)) {
          toolBlackList(imcMessage);
        }
      }
    }
  }
  
  private static void toolBlackList(IMCMessage imc) {
    NBTTagCompound nbt = imc.getNBTValue();
    int blockID = nbt.getInteger("blockID");
    if (blockID != 0 && ItemStackUtil.isBlock(blockID)) {
      QuantumToolBlackList.add(Block.blocksList[blockID]);
      Log.debug("[IMC] Added id " + blockID + " to QuantumToolBlackList");
    }
  }
  
  private static void addQMCItemStack(IMCMessage imc) {
    NBTTagCompound nbt = imc.getNBTValue();
    double qmc = nbt.getDouble("qmc");
    Object o = null;
    if (nbt.hasKey("ore")) {
      o = nbt.getString("ore");
    } else {
      o = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("item"));
    }
    if (o != null && qmc > 0) {
      QMC.add(o, qmc);
      Log.debug("[IMC] Added QMC to " + o.toString() + ", value " + qmc + " " + QMC.name + ", from " + imc.getSender());
    }
  }
}
