package oe.network.proxy;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

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
  
  @Override
  public boolean isClient() {
    return true;
  }
  
  @Override
  public String getCurrentLanguage() {
    return Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
  }
  
  @Override
  public void addLocalization(String key, String string) {
    LanguageRegistry.instance().addStringLocalization(key, string);
  }
}
