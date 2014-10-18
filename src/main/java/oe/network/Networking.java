package oe.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class Networking {

  public static SimpleNetworkWrapper network;

  public static void preInit() {
    network = NetworkRegistry.INSTANCE.newSimpleChannel("MyChannel");
    network.registerMessage(NBTPacketHandler.class, NBTPacket.class, 0, Side.CLIENT);
    network.registerMessage(NBTPacketHandler.class, NBTPacket.class, 0, Side.SERVER);
  }
}
