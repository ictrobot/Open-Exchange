package oe.network.packet;

import oe.item.ItemMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ChatMessageComponent;
import cpw.mods.fml.common.network.Player;

public class ItemModePacket {
  public static void packet(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity) {
    EntityPlayer player = (EntityPlayer) playerEntity;
    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemMode) {
      ItemMode m = (ItemMode) player.getHeldItem().getItem();
      player.sendChatToPlayer(ChatMessageComponent.createFromText("\u00A73\u00A7lItem Mode:\u00A7r\u00A77 " + m.switchMode(player.getHeldItem())));
    }
  }
}
