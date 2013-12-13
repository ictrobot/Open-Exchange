package oe.network.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import oe.OpenExchange;
import oe.core.Debug;
import oe.core.Log;
import oe.core.util.ConfigUtil;
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
    
    ConfigUtil.load();
    boolean dump = ConfigUtil.other("DEBUG", "Dump Server-->Client QMC Snapshots", false);
    ConfigUtil.save();
    
    try {
      if (dump) {
        String path = OpenExchange.configdir.toString() + "/../OE ServerToClient QMC Snapshot.dat";
        Log.debug(path);
        CompressedStreamTools.write(nbt, new File(path));
      }
    } catch (Exception e) {
      Debug.handleException(e);
    }
  }
}
