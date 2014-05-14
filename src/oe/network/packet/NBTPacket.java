package oe.network.packet;

import java.io.IOException;
import cpw.mods.fml.relauncher.Side;
import oe.core.util.NetworkUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class NBTPacket extends AbstractPacket {
  
  NBTTagCompound nbt;
  
  public NBTPacket() {
    
  }
  
  public NBTPacket(NBTTagCompound nbt) {
    this.nbt = nbt;
  }
  
  @Override
  public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
    try {
      byte[] b = CompressedStreamTools.compress(nbt);
      buffer.writeInt(b.length);
      buffer.writeBytes(b);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
    try {
      int i = buffer.readInt();
      nbt = CompressedStreamTools.decompress(buffer.readBytes(new byte[i]).array());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public void handleClientSide(EntityPlayer player) {
    NetworkUtil.handlePacket(nbt, player, Side.CLIENT);
  }
  
  @Override
  public void handleServerSide(EntityPlayer player) {
    NetworkUtil.handlePacket(nbt, player, Side.SERVER);
  }
  
}
