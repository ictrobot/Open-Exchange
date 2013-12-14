package oe.core.handler.keybind;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import oe.api.OEItemMode;
import oe.core.Debug;
import org.lwjgl.input.Keyboard;

public class OEKeyItemMode extends OEKeyBinding {
  
  public OEKeyItemMode() {
    super("OE Item Mode", Keyboard.KEY_M);
  }
  
  public void keyDown() {
    EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
    if (player == null) {
      return; // Not in game
    }
    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof OEItemMode) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
      DataOutputStream outputStream = new DataOutputStream(bos);
      try {
        outputStream.writeBoolean(true);
      } catch (Exception e) {
        Debug.handleException(e);
      }
      
      Packet250CustomPayload packet = new Packet250CustomPayload();
      packet.channel = "OpenExchangeIM";
      packet.data = bos.toByteArray();
      packet.length = bos.size();
      player.sendQueue.addToSendQueue(packet);
    }
  }
}
