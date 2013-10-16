package oe.api;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;

public class OEPacketAPI {
  public static boolean setValue(ItemStack stack, int Value) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(bos);
    try {
      dos.writeInt(1);
      dos.writeInt(Value);
    } catch (Exception e) {
      return false;
    }
    try {
      PacketTools.writeItemStack(stack, dos);
    } catch (Exception e) {
      return false;
    }
    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.channel = "OEValues";
    packet.data = bos.toByteArray();
    packet.length = bos.size();
    PacketDispatcher.sendPacketToServer(packet);
    return true;
  }
  
  
}
