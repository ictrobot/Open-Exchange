package oe.core.data;

import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import oe.core.util.NetworkUtil;
import oe.core.util.NetworkUtil.Channel;
import oe.core.util.NetworkUtil.PacketProcessor;
import oe.core.util.Util;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;

public class TileSync implements PacketProcessor {
  
  @SubscribeEvent
  public void tickServer(ServerTickEvent tick) {
    if (tick.phase == Phase.END) {
      syncServer();
    }
  }
  
  @SubscribeEvent
  public void tickClient(ClientTickEvent tick) {
    if (tick.phase == Phase.END) {
      syncClient();
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
  
  public static void syncServer() {
    WorldServer[] worlds = MinecraftServer.getServer().worldServers;
    if (worlds == null) {
      return;
    }
    for (WorldServer world : worlds) {
      if (world != null) {
        sync(world);
      }
    }
  }
  
  public static void syncClient() {
    WorldClient world = Minecraft.getMinecraft().theWorld;
    if (world == null) {
      return;
    }
    sync(world);
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
    HashMap<EntityPlayerMP, NBTTagCompound> packets = new HashMap<EntityPlayerMP, NBTTagCompound>();
    List<?> players = world.playerEntities;
    for (Object p : players) {
      EntityPlayerMP player = (EntityPlayerMP) p;
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
      nbt.setTag("data", data.get(tile));
      for (Object player : players) {
        if (((EntityPlayer) player).getDistance(tile.xCoord, tile.yCoord, tile.zCoord) <= range) {
          NBTTagCompound n = packets.get((EntityPlayer) player);
          n.setInteger("size", n.getInteger("size") + 1);
          int num = n.getInteger("size");
          n.setTag("" + num, nbt);
        }
      }
    }
    for (Object p : players) {
      NetworkUtil.sendToClient((EntityPlayerMP) p, Channel.TileSync, packets.get((EntityPlayer) p));
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
      nbt.setTag("data", data.get(tile));
      packetNBT.setInteger("size", packetNBT.getInteger("size") + 1);
      int num = packetNBT.getInteger("size");
      packetNBT.setTag("" + num, nbt);
    }
    // Keyset
    if (packetNBT.func_150296_c().size() == 1) {
      return;
    }
    NetworkUtil.sendToServer(Channel.TileSync, packetNBT);
  }
  
  @Override
  public void handlePacket(NBTTagCompound nbt, EntityPlayer player, Side side) {
    for (int i = 0; i <= nbt.getInteger("size"); i++) {
      NBTTagCompound data = nbt.getCompoundTag("" + i);
      TileEntity tile = player.worldObj.getTileEntity(data.getInteger("x"), data.getInteger("y"), data.getInteger("z"));
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
