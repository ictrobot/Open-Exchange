package oe.core.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import oe.block.tile.TileNetwork;
import oe.core.Debug;
import oe.core.Log;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public class TileSync {
  
  public static class Data {
    TileEntity tile;
    List<EntityPlayer> players;
    NBTTagCompound snapshot;
    
    public Data(TileEntity tile, List<EntityPlayer> players, NBTTagCompound snapshot) {
      this.tile = tile;
      this.players = players;
      this.snapshot = snapshot;
    }
  }
  
  public static class OETileSync implements ITickHandler {
    private final EnumSet<TickType> ticksToGet = EnumSet.of(TickType.WORLD);
    
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
  
  private static final double range = 64;
  
  public static void sync() {
    WorldServer[] worlds = MinecraftServer.getServer().worldServers;
    for (WorldServer world : worlds) {
      sync(world);
    }
  }
  
  public static void sync(WorldServer world) {
    // Get Data
    Data[] data = new Data[0];
    Object[] loaded = (world.loadedTileEntityList).toArray();
    List<?> players = world.playerEntities;
    for (Object o : loaded) {
      if (o instanceof TileEntity && o instanceof TileNetwork) {
        TileEntity tile = (TileEntity) o;
        List<EntityPlayer> p = new ArrayList<EntityPlayer>();
        NBTTagCompound snapshot = ((TileNetwork) o).networkSnapshot();
        for (Object player : players) {
          if (((EntityPlayer) player).getDistance(tile.xCoord, tile.yCoord, tile.zCoord) <= range) {
            p.add(((EntityPlayer) player));
          }
        }
        Data[] tmp = new Data[data.length + 1];
        System.arraycopy(data, 0, tmp, 0, data.length);
        data = tmp;
        data[data.length - 1] = new Data(tile, p, snapshot);
      }
    }
    // Distribute
    HashMap<EntityPlayer, NBTTagCompound> packets = new HashMap<EntityPlayer, NBTTagCompound>();
    for (Object p : players) {
      EntityPlayer player = (EntityPlayer) p;
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("size", -1);
      packets.put(player, nbt);
    }
    for (Data d : data) {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("x", d.tile.xCoord);
      nbt.setInteger("y", d.tile.yCoord);
      nbt.setInteger("z", d.tile.zCoord);
      nbt.setString("type", d.tile.getClass().getSimpleName());
      nbt.setCompoundTag("data", d.snapshot);
      for (Object p : players) {
        NBTTagCompound n = packets.get((EntityPlayer) p);
        n.setInteger("size", n.getInteger("size") + 1);
        int num = n.getInteger("size");
        n.setCompoundTag("" + num, nbt);
      }
    }
    for (Object p : players) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
      DataOutputStream outputStream = new DataOutputStream(bos);
      try {
        CompressedStreamTools.writeCompressed(packets.get((EntityPlayer) p), outputStream);
      } catch (IOException e) {
        Debug.handleException(e);
      }
      Packet250CustomPayload packet = new Packet250CustomPayload();
      packet.channel = "oeTileSync";
      packet.data = bos.toByteArray();
      packet.length = bos.size();
      PacketDispatcher.sendPacketToPlayer(packet, (Player) p);
    }
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
      if (tile != null && tile instanceof TileNetwork && data.getString("type").contentEquals(tile.getClass().getSimpleName())) {
        ((TileNetwork) tile).restoreSnapshot(data.getCompoundTag("data"));
      }
    }
  }
}
