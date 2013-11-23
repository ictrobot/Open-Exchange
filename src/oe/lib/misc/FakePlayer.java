package oe.lib.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.network.packet.Packet204ClientInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatBase;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;

public class FakePlayer extends EntityPlayerMP {
  public FakePlayer(World world, String name) {
    super(FMLCommonHandler.instance().getMinecraftServerInstance(), world, name, new ItemInWorldManager(world));
  }
  
  public void sendChatToPlayer(String s) {
  }
  
  @Override
  public boolean canCommandSenderUseCommand(int i, String s) {
    return false;
  }
  
  @Override
  public ChunkCoordinates getPlayerCoordinates() {
    return new ChunkCoordinates(0, 0, 0);
  }
  
  @Override
  public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent) {
  }
  
  @Override
  public void addStat(StatBase par1StatBase, int par2) {
  }
  
  @Override
  public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
  }
  
  @Override
  public boolean isEntityInvulnerable() {
    return true;
  }
  
  @Override
  public boolean canAttackPlayer(EntityPlayer player) {
    return false;
  }
  
  @Override
  public void onDeath(DamageSource source) {
    return;
  }
  
  @Override
  public void onUpdate() {
    return;
  }
  
  @Override
  public void travelToDimension(int dim) {
    return;
  }
  
  @Override
  public void updateClientInfo(Packet204ClientInfo pkt) {
    return;
  }
  
  public static class OEFakePlayer extends FakePlayer {
    
    public OEFakePlayer() {
      this("[OE]");
    }
    
    public OEFakePlayer(String str) {
      super(MinecraftServer.getServer().worldServers[0], str);
    }
  }
}
