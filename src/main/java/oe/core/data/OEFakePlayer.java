package oe.core.data;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import oe.core.Reference;

import java.util.UUID;

public class OEFakePlayer extends FakePlayer {

  private static GameProfile gameProfile = new GameProfile(UUID.fromString("4ba76cc0-5630-11e4-8ed6-0800200c9a66"), "[" + Reference.MOD_ID + "]");

  public OEFakePlayer() {
    this(MinecraftServer.getServer().worldServers[0]);
  }

  public OEFakePlayer(WorldServer world) {
    super(world, gameProfile);
  }

  //new GameProfile(name, name)
}
