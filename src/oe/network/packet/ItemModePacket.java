package oe.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import oe.api.OEItemMode;
import oe.core.util.NetworkUtil.PacketProcessor;
import oe.core.util.Util;
import cpw.mods.fml.relauncher.Side;

public class ItemModePacket implements PacketProcessor {
  @Override
  public void handlePacket(NBTTagCompound nbt, EntityPlayer player, Side side) {
    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof OEItemMode) {
      OEItemMode m = (OEItemMode) player.getHeldItem().getItem();
      Util.sendMsg(player, "\u00A73\u00A7l" + player.getHeldItem().getDisplayName() + " Mode:\u00A7r\u00A77 " + m.switchMode(player.getHeldItem()));
    }
  }
}
