package oe.network.packet;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import oe.core.util.NetworkUtil.PacketProcessor;
import oe.qmc.InWorldQMC;

public class TileInfoPacket implements PacketProcessor {

  @Override
  public void handlePacket(NBTTagCompound nbt, EntityPlayer player, Side side) {
    int x = nbt.getInteger("x");
    int y = nbt.getInteger("y");
    int z = nbt.getInteger("z");
    InWorldQMC.info(player, x, y, z);
  }
}
