package oe.lib.handler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import oe.OpenExchange;
import oe.lib.Debug;
import oe.lib.Log;
import oe.lib.helper.Sided;
import oe.qmc.QMC;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PlayerTracker implements IPlayerTracker {
  
  @Override
  public void onPlayerLogin(EntityPlayer player) {
    if (Sided.isServer()) {
      if (OpenExchange.proxy.isSinglePlayer()) {
        return;
      }
      try {
        sendReset(player);
        NBTTagCompound nbt = QMC.snapshot("Server --> Client");
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        CompressedStreamTools.writeCompressed(nbt, outputStream);
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "oeQMC";
        packet.data = bos.toByteArray();
        packet.length = bos.size();
        PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
        Log.debug("Sent QMC packet to " + player.username);
      } catch (Exception e) {
        Debug.handleException(e);
        Log.severe("Failed to send QMC packet to " + player.username);
      }
    }
  }
  
  public void sendReset(EntityPlayer Player) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
    DataOutputStream outputStream = new DataOutputStream(bos);
    try {
      outputStream.writeInt(0);
    } catch (Exception e) {
      Debug.handleException(e);
    }
    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.channel = "oeQMCReset";
    packet.data = bos.toByteArray();
    packet.length = bos.size();
    PacketDispatcher.sendPacketToPlayer(packet, (Player) Player);
    Log.debug("Sent QMCReset packet to " + Player.username);
  }
  
  @Override
  public void onPlayerLogout(EntityPlayer player) {
  }
  
  @Override
  public void onPlayerChangedDimension(EntityPlayer player) {
    
  }
  
  @Override
  public void onPlayerRespawn(EntityPlayer player) {
    
  }
}
