package oe.network.packet;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import oe.lib.Log;
import oe.qmc.QMC;
import cpw.mods.fml.common.network.Player;

public class QMCResetPacket {
  
  public static void packet(INetworkManager manager, Packet250CustomPayload packet, Player Player) {
    Log.debug("Received QMC Wipe Packet");
    QMC.restoreSnapshot(QMC.postInitSnapshot);
  }
}
