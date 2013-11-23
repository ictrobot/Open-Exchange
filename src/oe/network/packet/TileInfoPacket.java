package oe.network.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import oe.api.OEPipeInterface;
import oe.api.OETileInterface;
import oe.api.OE_API;
import oe.qmc.InWorldQMC;
import oe.qmc.QMC;
import cpw.mods.fml.common.network.Player;

public class TileInfoPacket {
  
  public static void packet(INetworkManager manager, Packet250CustomPayload packet, Player Player) {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    int x;
    int y;
    int z;
    
    try {
      x = inputStream.readInt();
      y = inputStream.readInt();
      z = inputStream.readInt();
    } catch (IOException e) {
      return;
    }
    if (Player instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) Player;
      TileEntity te = player.worldObj.getBlockTileEntity(x, y, z);
      if (te != null) {
        if (OE_API.isOE(te.getClass())) {
          OETileInterface oe = (OETileInterface) te;
          player.sendChatToPlayer(ChatMessageComponent.createFromText("\u00A73\u00A7l" + QMC.name + " Info:\u00A7r\u00A77 " + oe.getQMC() + " " + QMC.name + ", " + oe.getMaxQMC() + " Max " + QMC.name + ", Type: " + oe.getType()));
        } else if (InWorldQMC.isOEPipe(te.getClass())) {
          OEPipeInterface oe = (OEPipeInterface) te;
          player.sendChatToPlayer(ChatMessageComponent.createFromText("\u00A73\u00A7l" + QMC.name + " Info:\u00A7r\u00A77 " + (oe.getMaxQMC() - oe.passThroughLeft()) + " " + QMC.name + ", " + oe.getMaxQMC() + " Max " + QMC.name));
        }
      }
    }
  }
}
