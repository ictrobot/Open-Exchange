package oe.network.packet;

import oe.api.OEItemMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ChatMessageComponent;
import cpw.mods.fml.common.network.Player;

public class ItemModePacket {
  public static void packet(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity) {
    EntityPlayer player = (EntityPlayer) playerEntity;
    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof OEItemMode) {
      OEItemMode m = (OEItemMode) player.getHeldItem().getItem();
      player.sendChatToPlayer(ChatMessageComponent.createFromText("\u00A73\u00A7l" + player.getHeldItem().getDisplayName() + " Mode:\u00A7r\u00A77 " + m.switchMode(player.getHeldItem())));
    }
  }
}
