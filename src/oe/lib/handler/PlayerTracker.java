package oe.lib.handler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import oe.OpenExchange;
import oe.lib.Debug;
import oe.lib.helper.Sided;
import oe.qmc.QMC;
import oe.qmc.QMCData;
import oe.qmc.QMCType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PlayerTracker implements IPlayerTracker {
  
  @Override
  public void onPlayerLogin(EntityPlayer player) {
    if (Sided.isServer()) {
      sendWipe(player);
      if (OpenExchange.proxy.isSinglePlayer()) {
        return;
      }
      for (QMCData d : QMC.getDataBase()) {
        if (d.type != QMCType.OreDictionary) {
          ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
          DataOutputStream outputStream = new DataOutputStream(bos);
          try {
            outputStream.writeInt(d.itemstack.itemID);
            outputStream.writeInt(d.itemstack.getItemDamage());
            outputStream.writeDouble(d.QMC);
          } catch (Exception e) {
            Debug.handleException(e);
          }
          Packet250CustomPayload packet = new Packet250CustomPayload();
          packet.channel = "oeQMC";
          packet.data = bos.toByteArray();
          packet.length = bos.size();
          PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
        }
      }
    }
  }
  
  public void sendWipe(EntityPlayer Player) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
    DataOutputStream outputStream = new DataOutputStream(bos);
    try {
      outputStream.writeInt(0);
    } catch (Exception e) {
      Debug.handleException(e);
    }
    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.channel = "oeQMCWipe";
    packet.data = bos.toByteArray();
    packet.length = bos.size();
    PacketDispatcher.sendPacketToPlayer(packet, (Player) Player);
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
