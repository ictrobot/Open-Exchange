package oe.network.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import oe.block.tile.TileCharging;
import oe.block.tile.TileCondenser;
import oe.block.tile.TileDrill;
import oe.block.tile.TileExtractor;
import oe.block.tile.TilePipe;
import oe.block.tile.TileStorage;
import oe.lib.Debug;
import cpw.mods.fml.common.network.Player;

public class InternalPacket {
  
  public static void packet(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity) {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    
    int packetSender;
    
    try {
      packetSender = inputStream.readInt();
    } catch (IOException e) {
      Debug.handleException(e);
      return;
    }
    if (playerEntity instanceof EntityPlayer) {
      EntityPlayer player = ((EntityPlayer) playerEntity);
      if (packetSender == 10) {
        updateCondenser(manager, packet, player);
      } else if (packetSender == 11) {
        updateCharging(manager, packet, player);
      } else if (packetSender == 12) {
        updateExtractor(manager, packet, player);
      } else if (packetSender == 13) {
        updateStorage(manager, packet, player);
      } else if (packetSender == 14) {
        updatePipe(manager, packet, player);
      } else if (packetSender == 15) {
        updateDrill(manager, packet, player);
      }
    }
  }
  
  @SuppressWarnings("unused")
  private static void updateDrill(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) {
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
      Debug.handleException(e);
      return;
    }
    TileEntity te = player.worldObj.getBlockTileEntity(x, y, z);
    if (te instanceof TileDrill) {
      TileDrill drill = (TileDrill) te;
      drill.stored = stored;
    }
  }
  
  @SuppressWarnings("unused")
  private static void updatePipe(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    int x;
    int y;
    int z;
    double pass;
    try {
      int sender = inputStream.readInt();
      x = inputStream.readInt();
      y = inputStream.readInt();
      z = inputStream.readInt();
      pass = inputStream.readDouble();
    } catch (IOException e) {
      Debug.handleException(e);
      return;
    }
    TileEntity te = player.worldObj.getBlockTileEntity(x, y, z);
    if (te instanceof TilePipe) {
      TilePipe pipe = (TilePipe) te;
      pipe.passThrough = pass;
    }
  }
  
  @SuppressWarnings("unused")
  private static void updateStorage(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) {
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
      Debug.handleException(e);
      return;
    }
    TileEntity te = player.worldObj.getBlockTileEntity(x, y, z);
    if (te instanceof TileStorage) {
      TileStorage storage = (TileStorage) te;
      storage.stored = stored;
    }
  }
  
  @SuppressWarnings("unused")
  public static void updateExtractor(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) {
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
      Debug.handleException(e);
      return;
    }
    TileEntity te = player.worldObj.getBlockTileEntity(x, y, z);
    if (te instanceof TileExtractor) {
      TileExtractor extractor = (TileExtractor) te;
      extractor.stored = stored;
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
      Debug.handleException(e);
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
      Debug.handleException(e);
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
