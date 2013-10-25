package oe.network.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class Client extends Server {
  @Override
  public void registerRenderers() {
    
  }
  
  @Override
  public int addArmor(String armor) {
    return RenderingRegistry.addNewArmourRendererPrefix(armor);
  }
}
