package oe.core.handler.keybind;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import oe.api.OEItemMode;
import oe.core.handler.keybind.OEKeyBindHandler.OEKeyBinding;
import oe.core.util.NetworkUtil;
import oe.core.util.NetworkUtil.Channel;
import org.lwjgl.input.Keyboard;

public class OEKeyItemMode implements OEKeyBinding {
  
  public void keyDown() {
    EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
    if (player == null) {
      return; // Not in game
    }
    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof OEItemMode) {
      NetworkUtil.sendToServer(Channel.ItemMode, new NBTTagCompound());
    }
  }
  
  @Override
  public int key() {
    return Keyboard.KEY_M;
  }
}
