package oe.network.proxy;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class Client extends Server {
  @Override
  public void registerRenderers() {
    
  }
  
  @Override
  public int addArmor(String armor) {
    return RenderingRegistry.addNewArmourRendererPrefix(armor);
  }
  
  @Override
  public boolean isSinglePlayer() {
    return Minecraft.getMinecraft().isSingleplayer();
  }
  
  public boolean isDedicatedServer() {
    return false;
  }
  
  @Override
  public boolean isClient() {
    return true;
  }
}
