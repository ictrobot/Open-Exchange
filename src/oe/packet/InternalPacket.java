package oe.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import oe.block.tile.TileCharging;
import oe.block.tile.TileCondenser;
import cpw.mods.fml.common.network.Player;

public class InternalPacket {
  
  public static void packet(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity) {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    
    int packetSender;
    
    try {
      packetSender = inputStream.readInt();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    if (playerEntity instanceof EntityPlayer) {
      EntityPlayer player = ((EntityPlayer) playerEntity);
      if (packetSender == 10) {
        updateCondenser(manager, packet, player);
      } else if (packetSender == 11) {
        updateCharging(manager, packet, player);
      }
    }
  }
  
  @SuppressWarnings("unused")
  public static void updateCharging(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    int x;
    int y;
    int z;
    double stored;
    try {
      int sender = inputStream.readInt();
      x = inputStream.readInt();
      y = inputStream.readInt();
      z = inputStream.readInt();
      stored = inputStream.readDouble();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    TileEntity te = player.worldObj.getBlockTileEntity(x, y, z);
    if (te instanceof TileCharging) {
      TileCharging charging = (TileCharging) te;
      charging.stored = stored;
    }
  }
  
  @SuppressWarnings("unused")
  public static void updateCondenser(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    int x;
    int y;
    int z;
    double stored;
    boolean hasTarget;
    try {
      int sender = inputStream.readInt();
      x = inputStream.readInt();
      y = inputStream.readInt();
      z = inputStream.readInt();
      stored = inputStream.readDouble();
      hasTarget = inputStream.readBoolean();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    TileEntity te = player.worldObj.getBlockTileEntity(x, y, z);
    if (te instanceof TileCondenser) {
      TileCondenser condenser = (TileCondenser) te;
      condenser.stored = stored;
      condenser.hasTarget = hasTarget;
    }
  }
}
