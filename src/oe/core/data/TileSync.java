package oe.core.data;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.Player;
import oe.core.Debug;
import oe.core.Log;
import oe.core.util.NetworkUtil;
import oe.core.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public class TileSync {
  
  public static class OETileSync implements ITickHandler {
    private final EnumSet<TickType> ticksToGet = EnumSet.of(TickType.CLIENT, TickType.SERVER);
    
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }
    
    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
      TileSync.sync();
    }
    
    @Override
    public EnumSet<TickType> ticks() {
      return ticksToGet;
    }
    
    @Override
    public String getLabel() {
      return "OETileSync";
    }
  }
  
  /**
   * Server --> Client
   */
  public interface ServerNetworkedTile {
    
    public NBTTagCompound snapshotServer();
    
    public void restoreClient(NBTTagCompound nbt);
  }
  
  /**
   * Client --> Server
   */
  public interface ClientNetworkedTile {
    
    public NBTTagCompound snapshotClient();
    
    public void restoreServer(NBTTagCompound nbt);
  }
  
  private static final double range = 64;
  
  public static void sync() {
    try {
      if (Util.isServerSide()) {
        WorldServer[] worlds = MinecraftServer.getServer().worldServers;
        if (worlds == null) {
          return;
        }
        for (WorldServer world : worlds) {
          if (world != null) {
            sync(world);
          }
        }
      } else {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        if (world == null) {
          return;
        }
        sync(world);
      }
    } catch (Exception e) {
      Debug.handleException(e);
    }
  }
  
  private static void sync(WorldServer world) {
    if (!Util.isServerSide()) {
      return;
    }
    HashMap<TileEntity, NBTTagCompound> data = new HashMap<TileEntity, NBTTagCompound>();
    // Get Data
    Object[] loaded = (world.loadedTileEntityList).toArray();
    for (Object o : loaded) {
      if (o instanceof TileEntity && o instanceof ServerNetworkedTile) {
        TileEntity tile = (TileEntity) o;
        NBTTagCompound snapshot = ((ServerNetworkedTile) o).snapshotServer();
        if (!snapshot.hasNoTags()) {
          data.put(tile, snapshot);
        }
      }
    }
    // Distribute to Players
    HashMap<EntityPlayer, NBTTagCompound> packets = new HashMap<EntityPlayer, NBTTagCompound>();
    List<?> players = world.playerEntities;
    for (Object p : players) {
      EntityPlayer player = (EntityPlayer) p;
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("size", -1);
      packets.put(player, nbt);
    }
    for (TileEntity tile : data.keySet()) {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("x", tile.xCoord);
      nbt.setInteger("y", tile.yCoord);
      nbt.setInteger("z", tile.zCoord);
      nbt.setString("type", tile.getClass().getSimpleName());
      nbt.setCompoundTag("data", data.get(tile));
      for (Object player : players) {
        if (((EntityPlayer) player).getDistance(tile.xCoord, tile.yCoord, tile.zCoord) <= range) {
          NBTTagCompound n = packets.get((EntityPlayer) player);
          n.setInteger("size", n.getInteger("size") + 1);
          int num = n.getInteger("size");
          n.setCompoundTag("" + num, nbt);
        }
      }
    }
    for (Object p : players) {
      NetworkUtil.sendToClient((Player) p, "TS", packets.get((EntityPlayer) p));
    }
  }
  
  private static void sync(WorldClient world) {
    if (!Util.isClientSide()) {
      return;
    }
    HashMap<TileEntity, NBTTagCompound> data = new HashMap<TileEntity, NBTTagCompound>();
    // Get Data
    Object[] loaded = (world.loadedTileEntityList).toArray();
    for (Object o : loaded) {
      if (o instanceof TileEntity && o instanceof ClientNetworkedTile) {
        TileEntity tile = (TileEntity) o;
        NBTTagCompound snapshot = ((ClientNetworkedTile) o).snapshotClient();
        if (!snapshot.hasNoTags()) {
          data.put(tile, snapshot);
        }
      }
    }
    // Distribute to Server
    NBTTagCompound packetNBT = new NBTTagCompound();
    packetNBT.setInteger("size", -1);
    
    for (TileEntity tile : data.keySet()) {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("x", tile.xCoord);
      nbt.setInteger("y", tile.yCoord);
      nbt.setInteger("z", tile.zCoord);
      nbt.setString("type", tile.getClass().getSimpleName());
      nbt.setCompoundTag("data", data.get(tile));
      packetNBT.setInteger("size", packetNBT.getInteger("size") + 1);
      int num = packetNBT.getInteger("size");
      packetNBT.setCompoundTag("" + num, nbt);
    }
    if (packetNBT.getTags().size() == 1) {
      return;
    }
    NetworkUtil.sendToServer(Minecraft.getMinecraft().thePlayer, "TS", packetNBT);
  }
  
  public static void packet(INetworkManager manager, Packet250CustomPayload packet, Player playerentity) {
    EntityPlayer player = ((EntityPlayer) playerentity);
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    NBTTagCompound nbt;
    try {
      nbt = CompressedStreamTools.readCompressed(inputStream);
    } catch (Exception e) {
      Debug.handleException(e);
      Log.severe("Failed to read TileSync packet");
      return;
    }
    
    for (int i = 0; i <= nbt.getInteger("size"); i++) {
      NBTTagCompound data = nbt.getCompoundTag("" + i);
      TileEntity tile = player.worldObj.getBlockTileEntity(data.getInteger("x"), data.getInteger("y"), data.getInteger("z"));
      if (Util.isClientSide()) {
        if (tile != null && tile instanceof ServerNetworkedTile && data.getString("type").contentEquals(tile.getClass().getSimpleName())) {
          ((ServerNetworkedTile) tile).restoreClient(data.getCompoundTag("data"));
        }
      } else {
        if (tile != null && tile instanceof ClientNetworkedTile && data.getString("type").contentEquals(tile.getClass().getSimpleName())) {
          ((ClientNetworkedTile) tile).restoreServer(data.getCompoundTag("data"));
        }
      }
    }
  }
}
