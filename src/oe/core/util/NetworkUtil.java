package oe.core.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import oe.core.Debug;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class NetworkUtil {
  
  public static final String baseChannel = "OpenExchange";
  
  public static final String[] channels = new String[] { // OE network channels
    "OpenExchange", // Internal
    "OpenExchangeTS", // TileSync
    "OpenExchangeIM", // ItemMode
    "OpenExchangeQD", // QuantumDestruction
    "OpenExchangeBM", // BlockManipulater
    "OpenExchangeQMC", // QMC Server --> Client Snapshot
    "OpenExchangeT" // TileInfo
  };
  
  public static boolean sendToClient(Player player, String channel, NBTTagCompound nbt) {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
      DataOutputStream outputStream = new DataOutputStream(bos);
      CompressedStreamTools.writeCompressed(nbt, outputStream);
      Packet250CustomPayload packet = new Packet250CustomPayload();
      packet.channel = baseChannel + channel;
      packet.data = bos.toByteArray();
      packet.length = bos.size();
      PacketDispatcher.sendPacketToPlayer(packet, player);
      return true;
    } catch (Exception e) {
      Debug.handleException(e);
      return false;
    }
  }
  
  public static boolean sendToServer(EntityClientPlayerMP player, String channel, NBTTagCompound nbt) {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
      DataOutputStream outputStream = new DataOutputStream(bos);
      CompressedStreamTools.writeCompressed(nbt, outputStream);
      Packet250CustomPayload packet = new Packet250CustomPayload();
      packet.channel = baseChannel + channel;
      packet.data = bos.toByteArray();
      packet.length = bos.size();
      player.sendQueue.addToSendQueue(packet);
      return true;
    } catch (Exception e) {
      Debug.handleException(e);
      return false;
    }
  }
}
