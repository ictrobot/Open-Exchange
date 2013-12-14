package oe.network.connection;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import oe.OpenExchange;
import oe.core.Debug;
import oe.core.Log;
import oe.core.util.Util;
import oe.qmc.QMC;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ConnectionHandler implements IConnectionHandler {
  @Override
  public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
    if (OpenExchange.proxy.isSinglePlayer()) {
      return;
    }
    try {
      NBTTagCompound nbt = QMC.snapshot("Server --> Client");
      ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
      DataOutputStream outputStream = new DataOutputStream(bos);
      CompressedStreamTools.writeCompressed(nbt, outputStream);
      Packet250CustomPayload packet = new Packet250CustomPayload();
      packet.channel = "oeQMC";
      packet.data = bos.toByteArray();
      packet.length = bos.size();
      PacketDispatcher.sendPacketToPlayer(packet, player);
      Log.debug("Sent QMC packet to " + ((EntityPlayer) player).username);
    } catch (Exception e) {
      Debug.handleException(e);
      Log.severe("Failed to send QMC packet to " + ((EntityPlayer) player).username);
    }
  }
  
  @Override
  public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
    return "";
  }
  
  @Override
  public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {
    
  }
  
  @Override
  public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {
    
  }
  
  @Override
  public void connectionClosed(INetworkManager manager) {
    if (Util.isClient() && Util.isClientSide()) {
      QMC.restoreSnapshot(QMC.getSave().QMCSnapshot);
    }
  }
  
  @Override
  public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
    
  }
}
