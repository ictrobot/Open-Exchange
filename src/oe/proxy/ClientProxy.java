package oe.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
  @Override
  public void registerRenderers() {
    
  }
  
  @Override
  public int addArmor(String armor) {
    return RenderingRegistry.addNewArmourRendererPrefix(armor);
  }
}
