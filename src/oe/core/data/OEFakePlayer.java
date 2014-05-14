package oe.core.data;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import oe.core.Reference;
import com.mojang.authlib.GameProfile;

public class OEFakePlayer extends FakePlayer {
  
  public OEFakePlayer() {
    this(MinecraftServer.getServer().worldServers[0], Reference.MOD_NAME);
  }
  
  public OEFakePlayer(String name) {
    this(MinecraftServer.getServer().worldServers[0], name);
  }
  
  public OEFakePlayer(WorldServer world, String name) {
    super(world, new GameProfile(name, name));
  }
}
