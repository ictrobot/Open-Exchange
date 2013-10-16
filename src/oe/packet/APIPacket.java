package oe.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import oe.Log;
import oe.api.PacketTools;
import oe.value.Values;
import cpw.mods.fml.common.network.Player;

public class APIPacket {
  
  public static void packet(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity) {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    
    int t;
    
    try {
      t = inputStream.readInt();
    } catch (IOException e) {
      return;
    }
    if (playerEntity instanceof EntityPlayer) {
      EntityPlayer player = ((EntityPlayer) playerEntity);
      if (t == 1) {
        setValue(manager, packet, player);
      }
    }
  }
  
  @SuppressWarnings("unused")
  public static void setValue(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    ItemStack stack;
    int v;
    try {
      int t = inputStream.readInt();
      v = inputStream.readInt();;
      stack = PacketTools.readItemStack(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    Log.debug("Adding " + stack.toString() + " Value " + v + " from Packet API");
    Values.add(stack, v);
  }
}
