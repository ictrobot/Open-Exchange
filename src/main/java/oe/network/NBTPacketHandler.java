package oe.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import oe.core.util.NetworkUtil;

public class NBTPacketHandler implements IMessageHandler<NBTPacket, IMessage> {
  @Override
  public IMessage onMessage(NBTPacket message, MessageContext ctx) {
    String channelStr = message.nbt.getString("channel");
    NetworkUtil.Channel c = NetworkUtil.Channel.valueOf(channelStr);
    if (ctx.side == Side.SERVER) {
      c.p.handlePacket(message.nbt.getCompoundTag("data"), ctx.getServerHandler().playerEntity, ctx.side);
    } else {
      c.p.handlePacket(message.nbt.getCompoundTag("data"), Minecraft.getMinecraft().thePlayer, ctx.side);
    }
    return null;
  }
}
