package oe.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import oe.OpenExchange;
import oe.item.ItemBlockManipulator;
import oe.item.OEItems;
import oe.network.packet.ItemModePacket;
import oe.network.packet.NBTPacket;
import oe.network.packet.QMCSynchronizationPacket;
import oe.network.packet.QuantumDestructionPacket;
import oe.network.packet.TileInfoPacket;
import cpw.mods.fml.relauncher.Side;

public class NetworkUtil {
  
  public static interface PacketProcessor {
    public void handlePacket(NBTTagCompound nbt, EntityPlayer player, Side side);
  }
  
  public static final String BASE_CHANNEL = "OpenExchange";
  
  public static enum Channel {
    TileSync(new oe.core.data.TileSync()), ItemMode(new ItemModePacket()), QuantumDestruction(new QuantumDestructionPacket()), BlockManipulater((ItemBlockManipulator) OEItems.blockManipulator), ServerToClientSnapshot(new QMCSynchronizationPacket()), TileInfo(new TileInfoPacket());
    
    public final PacketProcessor p;
    
    private Channel(PacketProcessor p) {
      this.p = p;
    }
  }
  
  public static void sendToClient(EntityPlayerMP player, Channel channel, NBTTagCompound nbt) {
    NBTPacket p = new NBTPacket(getContainerNBT(channel, nbt));
    OpenExchange.packetPipeline.sendTo(p, player);
  }
  
  public static void sendToServer(Channel channel, NBTTagCompound nbt) {
    NBTPacket p = new NBTPacket(getContainerNBT(channel, nbt));
    OpenExchange.packetPipeline.sendToServer(p);
  }
  
  public static void handlePacket(NBTTagCompound nbt, EntityPlayer player, Side side) {
    String channelStr = nbt.getString("channel");
    Channel c = Channel.valueOf(channelStr);
    c.p.handlePacket(nbt, player, side);
  }
  
  private static NBTTagCompound getContainerNBT(Channel channel, NBTTagCompound nbt) {
    NBTTagCompound c = new NBTTagCompound();
    c.setTag("data", nbt);
    c.setString("channel", channel.toString());
    return c;
  }
  
  public static void sendMouseOverToServer(Channel channel, EntityPlayer player) {
    if (Minecraft.getMinecraft().objectMouseOver != null) {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("x", Minecraft.getMinecraft().objectMouseOver.blockX);
      nbt.setInteger("y", Minecraft.getMinecraft().objectMouseOver.blockY);
      nbt.setInteger("z", Minecraft.getMinecraft().objectMouseOver.blockZ);
      sendToServer(channel, nbt);
    }
  }
}
