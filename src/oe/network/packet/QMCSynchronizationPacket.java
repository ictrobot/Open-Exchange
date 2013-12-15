package oe.network.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import oe.core.Debug;
import oe.core.Log;
import oe.qmc.QMC;
import cpw.mods.fml.common.network.Player;

public class QMCSynchronizationPacket {
  
  public static void packet(INetworkManager manager, Packet250CustomPayload packet, Player Player) {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    NBTTagCompound nbt;
    
    try {
      nbt = CompressedStreamTools.readCompressed(inputStream);
      QMC.restoreSnapshot(nbt);
      Log.debug("Read QMC packet from server");
    } catch (Exception e) {
      Debug.handleException(e);
      Log.severe("Failed to read QMC packet from server");
      return;
    }
  }
}
