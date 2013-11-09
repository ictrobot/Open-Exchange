package oe.network.packet;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {
  
  @Override
  public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity) {
    if (packet.channel == "oe") {
      InternalPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel == "oeQD") {
      QuantumDestructionPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel == "oeTransmutation") {
      TransmutationPacket.packet(manager, packet, playerEntity);
    } else if (packet.channel == "oeBM") {
      BlockMoverPacket.packet(manager, packet, playerEntity);
    }
  }
}
