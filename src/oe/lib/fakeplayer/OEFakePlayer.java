package oe.lib.fakeplayer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatBase;
import net.minecraftforge.common.FakePlayer;

public class OEFakePlayer extends FakePlayer {
  
  public OEFakePlayer() {
    this("[OE]");
  }
  
  public OEFakePlayer(String str) {
    super(MinecraftServer.getServer().worldServers[0], str);
  }
  
  public void addStat(StatBase par1StatBase, int par2) {
  }
}
