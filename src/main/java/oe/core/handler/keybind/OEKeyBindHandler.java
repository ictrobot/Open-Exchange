package oe.core.handler.keybind;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class OEKeyBindHandler {

  private List<OEKeyBinding> bindings = new ArrayList<OEKeyBinding>();

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void keyPress(KeyInputEvent keyInput) {
    for (OEKeyBinding k : bindings) {
      if (Keyboard.isKeyDown(k.key())) {
        k.keyDown();
      }
    }
  }

  public static interface OEKeyBinding {
    public void keyDown();

    public int key();
  }

}
