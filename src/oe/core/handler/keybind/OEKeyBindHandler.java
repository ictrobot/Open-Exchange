package oe.core.handler.keybind;

import java.util.EnumSet;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class OEKeyBindHandler extends KeyHandler {
  static OEKeyBinding[] binds = { new OEKeyItemMode() };
  static boolean[] repeatings = { false };
  
  public OEKeyBindHandler() {
    super(binds, repeatings);
  }
  
  @Override
  public String getLabel() {
    return "OEKeyBind";
  }
  
  @Override
  public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
    if (tickEnd && FMLClientHandler.instance().getClient().inGameHasFocus) {
      if (kb instanceof OEKeyBinding) {
        ((OEKeyBinding) kb).keyDown();
      }
    }
  }
  
  @Override
  public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
    if (kb instanceof OEKeyBinding) {
      ((OEKeyBinding) kb).keyUp();
    }
  }
  
  @Override
  public EnumSet<TickType> ticks() {
    return EnumSet.of(TickType.CLIENT);
  }
}
