package oe.network.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import oe.qmc.QMC;
import oe.qmc.guess.GuessReturn;
import cpw.mods.fml.common.network.Player;

public class QMCSynchronizationPacket {
  
  public static void packet(INetworkManager manager, Packet250CustomPayload packet, Player Player) {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    int id;
    int meta;
    double value;
    
    try {
      id = inputStream.readInt();
      meta = inputStream.readInt();
      value = inputStream.readDouble();
    } catch (IOException e) {
      return;
    }
    ItemStack stack = new ItemStack(id, 1, meta);
    if (stack != null) {
      // Add as guess so it will be wiped on connection to next server
      GuessReturn guess = new GuessReturn(new ItemStack[] { stack }, new double[] { value }, value, 1);
      QMC.addGuessed(stack, guess);
    }
  }
}
