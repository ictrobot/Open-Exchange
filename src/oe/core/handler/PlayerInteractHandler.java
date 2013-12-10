package oe.core.handler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import oe.api.OEItemInterface;
import oe.core.Debug;
import oe.core.util.Util;
import oe.item.ItemIDs;

public class PlayerInteractHandler {
  
  @ForgeSubscribe
  public void onPlayerInteractEvent(PlayerInteractEvent event) {
    if (Util.isClientSide()) {
      if (event.action != Action.RIGHT_CLICK_BLOCK) {
        return;
      }
      EntityPlayer player = event.entityPlayer;
      if (player == null) {
        return;
      }
      if (player.getHeldItem() == null || player.getHeldItem().itemID != ItemIDs.blockMover + 256 || player.getHeldItem().stackTagCompound == null || !(player.getHeldItem().getItem() instanceof OEItemInterface)) {
        return;
      }
      int x = event.x;
      int y = event.y;
      int z = event.z;
      int blockFace = event.face; // Bottom = 0, Top = 1, Sides = 2-5
      packet(x, y, z, blockFace, player);
      event.setCanceled(true);
    }
  }
  
  private void packet(int x, int y, int z, int face, EntityPlayer tmpplayer) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
    DataOutputStream outputStream = new DataOutputStream(bos);
    try {
      outputStream.writeInt(x);
      outputStream.writeInt(y);
      outputStream.writeInt(z);
      outputStream.writeInt(face);
    } catch (Exception e) {
      Debug.handleException(e);
    }
    
    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.channel = "oeBM";
    packet.data = bos.toByteArray();
    packet.length = bos.size();
    EntityClientPlayerMP player = (EntityClientPlayerMP) tmpplayer;
    player.sendQueue.addToSendQueue(packet);
  }
}
