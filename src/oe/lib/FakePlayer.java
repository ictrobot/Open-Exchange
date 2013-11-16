package oe.lib;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatBase;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class FakePlayer extends EntityPlayerMP {
  public FakePlayer(World world, String name) {
    super(FMLCommonHandler.instance().getMinecraftServerInstance(), world, name, new ItemInWorldManager(world));
  }
  
  public void sendChatToPlayer(String s) {
  }
  
  public boolean canCommandSenderUseCommand(int i, String s) {
    return false;
  }
  
  public ChunkCoordinates getPlayerCoordinates() {
    return new ChunkCoordinates(0, 0, 0);
  }
  
  @Override
  public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent) {
  }
  
  public static class OEFakePlayer extends FakePlayer {
    
    public OEFakePlayer() {
      this("[OE]");
    }
    
    public OEFakePlayer(String str) {
      super(MinecraftServer.getServer().worldServers[0], str);
    }
    
    public void addStat(StatBase par1StatBase, int par2) {
    }
  }
}
