package oe.item;

import oe.core.util.ConfigUtil;

public class ItemIDs {
  
  public static int buildRing;
  public static int reader;
  public static int pickaxe;
  public static int axe;
  public static int shovel;
  public static int repair;
  public static int blockMover;
  
  public static void Load() {
    buildRing = ConfigUtil.item("buildRing");
    reader = ConfigUtil.item("reader");
    pickaxe = ConfigUtil.item("pickaxe");
    axe = ConfigUtil.item("axe");
    shovel = ConfigUtil.item("shovel");
    repair = ConfigUtil.item("repair");
    blockMover = ConfigUtil.item("blockMover");
  }
}
