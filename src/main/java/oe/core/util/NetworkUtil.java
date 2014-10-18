package oe.core.util;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import oe.item.ItemBlockManipulator;
import oe.item.OEItems;
import oe.network.NBTPacket;
import oe.network.Networking;
import oe.network.packet.ItemModePacket;
import oe.network.packet.QMCSynchronizationPacket;
import oe.network.packet.QuantumDestructionPacket;
import oe.network.packet.TileInfoPacket;

public class NetworkUtil {

  public static interface PacketProcessor {
    public void handlePacket(NBTTagCompound nbt, EntityPlayer player, Side side);
  }

  public static enum Channel {
    TileSync(new oe.core.data.TileSync()), ItemMode(new ItemModePacket()), QuantumDestruction(new QuantumDestructionPacket()), BlockManipulater((ItemBlockManipulator) OEItems.blockManipulator), ServerToClientSnapshot(new QMCSynchronizationPacket()), TileInfo(new TileInfoPacket());

    public final PacketProcessor p;

    private Channel(PacketProcessor p) {
      this.p = p;
    }
  }

  public static void sendToClient(EntityPlayerMP player, Channel channel, NBTTagCompound nbt) {
    NBTPacket p = new NBTPacket(getContainerNBT(channel, nbt));
    Networking.network.sendTo(p, player);
  }

  public static void sendToServer(Channel channel, NBTTagCompound nbt) {
    NBTPacket p = new NBTPacket(getContainerNBT(channel, nbt));
    Networking.network.sendToServer(p);
  }

  private static NBTTagCompound getContainerNBT(Channel channel, NBTTagCompound nbt) {
    NBTTagCompound c = new NBTTagCompound();
    c.setTag("data", nbt);
    c.setString("channel", channel.toString());
    return c;
  }

  public static void sendMouseOverToServer(Channel channel) {
    if (Minecraft.getMinecraft().objectMouseOver != null) {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("x", Minecraft.getMinecraft().objectMouseOver.blockX);
      nbt.setInteger("y", Minecraft.getMinecraft().objectMouseOver.blockY);
      nbt.setInteger("z", Minecraft.getMinecraft().objectMouseOver.blockZ);
      sendToServer(channel, nbt);
    }
  }
}
