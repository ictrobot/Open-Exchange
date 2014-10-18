package oe.core.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import oe.OpenExchange;

import java.util.List;

public class Util {

  public static boolean isServerSide() {
    return getSide().isServer();
  }

  public static boolean isClientSide() {
    return getSide().isClient();
  }

  public static boolean isClient() {
    return OpenExchange.proxy.isClient();
  }

  public static Side getSide() {
    return FMLCommonHandler.instance().getEffectiveSide();
  }

  public static String getTime() {
    return new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime());
  }

  public static String getDate() {
    return new java.text.SimpleDateFormat("dd,MM,yyyy").format(java.util.Calendar.getInstance().getTime());
  }

  public static boolean notEmpty(List<?> list) {
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }

  public static int minPos(int a, int b) {
    if (a < 0) {
      return b;
    }
    if (b < 0) {
      return a;
    }
    return Math.min(a, b);
  }

  public static double minPos(double a, double b) {
    if (a < 0) {
      return b;
    }
    if (b < 0) {
      return a;
    }
    return Math.min(a, b);
  }

  public static void sendMsg(ICommandSender sender, String msg) {
    sender.addChatMessage(new ChatComponentText(msg));
  }
}
