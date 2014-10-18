package oe.network.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;

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
