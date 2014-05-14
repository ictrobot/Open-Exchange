package oe.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import oe.core.Log;
import oe.core.util.NetworkUtil.PacketProcessor;
import oe.qmc.QMC;
import cpw.mods.fml.relauncher.Side;

public class QMCSynchronizationPacket implements PacketProcessor {
  
  @Override
  public void handlePacket(NBTTagCompound nbt, EntityPlayer player, Side side) {
    QMC.restoreSnapshot(nbt);
    Log.debug("Read QMC packet from server");
  }
}
