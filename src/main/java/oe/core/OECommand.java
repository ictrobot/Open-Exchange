package oe.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.OEItemInterface;
import oe.core.util.FluidUtil;
import oe.core.util.Util;
import oe.qmc.QMC;

import java.util.ArrayList;
import java.util.List;

public class OECommand implements ICommand {
  @SuppressWarnings("rawtypes")
  private List aliases;

  @SuppressWarnings({"rawtypes", "unchecked"})
  public OECommand() {
    this.aliases = new ArrayList();
    this.aliases.add("oe");
    this.aliases.add("OE");
  }

  @Override
  public String getCommandName() {
    return "oe";
  }

  @Override
  public String getCommandUsage(ICommandSender sender) {
    return "/" + this.getCommandName() + " help";
  }

  @SuppressWarnings("rawtypes")
  @Override
  public List getCommandAliases() {
    return this.aliases;
  }

  @Override
  public void processCommand(ICommandSender sender, String[] arguments) {
    if (arguments.length <= 0) {
      throw new WrongUsageException("Type '" + this.getCommandUsage(sender) + "' for help.");
    }
    if (arguments[0].toLowerCase().matches("version")) {
      commandVersion(sender, arguments);
      return;
    }
    if (arguments[0].toLowerCase().matches("help")) {
      commandHelp(sender, arguments);
      return;
    }
    if (arguments[0].toLowerCase().matches("data")) {
      commandData(sender, arguments);
      return;
    }
    if (arguments[0].toLowerCase().matches("fluid")) {
      commandFluid(sender, arguments);
      return;
    }
    if (arguments[0].toLowerCase().matches("mod")) {
      commandMod(sender, arguments);
      return;
    }
    throw new WrongUsageException("Type '" + this.getCommandUsage(sender) + "' for help.");
  }

  private void commandHelp(ICommandSender sender, String[] arguments) {
    sendMsg(sender, "Usage");
    sendMsg(sender, "/oe version  - Returns Open Exchange version");
    sendMsg(sender, "/oe data     - Returns info about the item in your hand");
    sendMsg(sender, "/oe fluid    - Returns info about the fluid container in your hand");
    sendMsg(sender, "/oe mod      - Returns what mod the item in your hand is from");
  }

  private void commandFluid(ICommandSender sender, String[] arguments) {
    EntityPlayerMP player = getPlayer(sender);
    if (player != null) {
      ItemStack held = player.getHeldItem();
      if (held != null) {
        sendMsg(sender, "--- ItemStack Fluid Data ---");
        if (FluidUtil.storesFluid(held)) {
          FluidStack f = FluidUtil.getFluidStack(held);
          ItemStack e = FluidUtil.getEmpty(held);
          if (f != null && e != null) {
            sendMsg(sender, "Fluid:");
            sendMsg(sender, "  Name: " + FluidUtil.getName(f.fluidID));
            sendMsg(sender, "  FluidID: " + f.fluidID);
            sendMsg(sender, "  Amount: " + f.amount);
            sendMsg(sender, "  QMC:");
            sendMsg(sender, "    QMC per 1000mb: " + QMC.getQMC(FluidUtil.getFluid(f.fluidID)));
            sendMsg(sender, "    QMC: " + QMC.getQMC(f));
            sendMsg(sender, "Empty Container:");
            sendMsg(sender, "  ID:" + held.getUnlocalizedName() + " Meta:" + held.getItemDamage());
          }
        } else {
          sendMsg(sender, "The held ItemStack does not store fluid");
        }
      }
    }
  }

  private void commandMod(ICommandSender sender, String[] arguments) {
    EntityPlayerMP player = getPlayer(sender);
    if (player != null) {
      ItemStack held = player.getHeldItem();
      if (held != null) {
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(held.getItem());
        sendMsg(sender, "--- ItemStack Mod Data ---");
        if (id != null) {
          sendMsg(sender, "Mod: " + id.modId);
          sendMsg(sender, "Name: " + id.name);
        } else {
          sendMsg(sender, "The held ItemStack is not registered correctly in the GameRegistery");
        }
      }
    }
  }

  private void commandData(ICommandSender sender, String[] arguments) {
    EntityPlayerMP player = getPlayer(sender);
    if (player != null) {
      ItemStack held = player.getHeldItem();
      if (held != null) {
        sendMsg(sender, "--- Open Exchange Data ---");
        if (QMC.hasQMC(held)) {
          sendMsg(sender, QMC.getQMC(held) + " " + QMC.name);
          sendMsg(sender, "ID:" + held.getUnlocalizedName() + " Meta:" + held.getItemDamage());
        } else {
          sendMsg(sender, "The held item does not have a value");
          sendMsg(sender, "ID:" + held.getUnlocalizedName() + " Meta:" + held.getItemDamage());
        }
        if (held.getItem() instanceof OEItemInterface) {
          OEItemInterface oe = (OEItemInterface) held.getItem();
          sendMsg(sender, "Stored " + oe.getQMC(held) + " " + QMC.name + ", Max " + oe.getMaxQMC(held) + " " + QMC.name);
        }
        int oreID = OreDictionary.getOreID(held);
        if (oreID != -1) {
          sendMsg(sender, "OreDictionary: " + OreDictionary.getOreName(oreID));
        }
      }
    }
  }

  private void commandVersion(ICommandSender sender, String[] arguments) {
    sendMsg(sender, Reference.NAME_DEBUG);
  }

  @Override
  public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
    return true;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {
    return null;
  }

  @Override
  public boolean isUsernameIndex(String[] astring, int i) {
    return false;
  }

  @Override
  public int compareTo(Object o) {
    return 0;
  }

  public static EntityPlayerMP getPlayer(ICommandSender sender) {
    if (!(sender instanceof EntityPlayerMP)) return null;
    return (EntityPlayerMP) sender;
  }

  public static void sendMsg(ICommandSender sender, String msg) {
    Util.sendMsg(sender, msg);
  }
}
