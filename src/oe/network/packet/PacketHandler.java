package oe.network.packet;

import oe.core.data.TileSync;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {
  
  @Override
  public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity) {
    if (packet.channel.contentEquals("OpenExchange")) {
      InternalPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel.contentEquals("OpenExchangeTS")) {
      TileSync.packet(manager, packet, playerEntity);
    } else if (packet.channel.contentEquals("OpenExchangeQD")) {
      QuantumDestructionPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel.contentEquals("OpenExchangeBM")) {
      BlockManipulatorPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel.contentEquals("OpenExchangeQMC")) {
      QMCSynchronizationPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel.contentEquals("OpenExchangeT")) {
      TileInfoPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel.contentEquals("OpenExchangeIM")) {
      ItemModePacket.packet(manager, packet, playerEntity);
    }
  }
}
