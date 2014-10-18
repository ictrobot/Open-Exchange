package oe.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class NBTPacket implements IMessage {

  NBTTagCompound nbt;

  public NBTPacket() {

  }

  public NBTPacket(NBTTagCompound nbt) {
    this.nbt = nbt;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    ByteBufUtils.writeTag(buf, nbt);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    nbt = ByteBufUtils.readTag(buf);
  }
}
