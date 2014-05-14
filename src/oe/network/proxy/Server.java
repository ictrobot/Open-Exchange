package oe.network.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;

public class Server {
  
  public void registerRenderers() {
    
  }
  
  public int addArmor(String armor) {
    return 0;
  }
  
  public void resetPlayerInAirTime(EntityPlayer player) {
    if (!(player instanceof EntityPlayerMP)) {
      return;
    }
    // floatingTickCount
    NetHandlerPlayServer nhps = ((EntityPlayerMP) player).playerNetServerHandler;
    try {
      nhps.getClass().getField("floatingTickCount").set(nhps, 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public boolean isSinglePlayer() {
    return false;
  }
  
  public boolean isDedicatedServer() {
    try {
      return FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer();
    } catch (Exception e) {
      return false;
    }
  }
  
  public boolean isClient() {
    return false;
  }
}
