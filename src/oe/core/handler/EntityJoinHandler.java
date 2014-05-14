package oe.core.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import oe.OpenExchange;
import oe.core.util.NetworkUtil;
import oe.core.util.NetworkUtil.Channel;
import oe.qmc.QMC;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntityJoinHandler {
  
  @SubscribeEvent
  public void onEntityJoinWorld(EntityJoinWorldEvent event) {
    final Entity entity = event.entity;
    if (!event.world.isRemote && entity instanceof EntityPlayer) {
      EntityPlayerMP player = (EntityPlayerMP) entity;
      if (OpenExchange.proxy.isSinglePlayer()) {
        return;
      }
      NetworkUtil.sendToClient(player, Channel.ServerToClientSnapshot, QMC.snapshot("Server --> Client"));
    }
  }
}
