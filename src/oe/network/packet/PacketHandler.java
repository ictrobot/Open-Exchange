package oe.network.packet;

import oe.core.data.TileSync;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {
  
  @Override
  public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity) {
    if (packet.channel.contentEquals("oe")) {
      InternalPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel.contentEquals("oeTileSync")) {
      TileSync.packet(manager, packet, playerEntity);
    } else if (packet.channel.contentEquals("oeQD")) {
      QuantumDestructionPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel.contentEquals("oeBM")) {
      BlockManipulatorPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel.contentEquals("oeQMC")) {
      QMCSynchronizationPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel.contentEquals("oeQMCReset")) {
      QMCResetPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel.contentEquals("oeTileInfo")) {
      TileInfoPacket.packet(manager, packet, playerEntity);
    }
  }
}
