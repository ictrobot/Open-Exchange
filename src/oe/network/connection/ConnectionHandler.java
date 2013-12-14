package oe.network.connection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import oe.OpenExchange;
import oe.core.Log;
import oe.core.util.NetworkUtil;
import oe.core.util.Util;
import oe.qmc.QMC;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class ConnectionHandler implements IConnectionHandler {
  @Override
  public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
    if (OpenExchange.proxy.isSinglePlayer()) {
      return;
    }
    if (NetworkUtil.sendToClient(player, "QMC", QMC.snapshot("Server --> Client"))) {
      Log.debug("Sent QMC packet to " + ((EntityPlayer) player).username);
    } else {
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
    if (Util.isClient() && Util.isClientSide() && QMC.getSave() != null && !OpenExchange.proxy.isSinglePlayer()) {
      QMC.restoreSnapshot(QMC.getSave().QMCSnapshot);
    }
  }
  
  @Override
  public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
    
  }
}
