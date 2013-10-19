package oe;

import java.util.ArrayList;
import java.util.List;
import oe.qmc.QMC;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;

public class OECommand implements ICommand {
  @SuppressWarnings("rawtypes")
  private List aliases;
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
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
    if (arguments[0].matches("version")) {
      commandVersion(sender, arguments);
      return;
    }
    if (arguments[0].matches("help")) {
      commandHelp(sender, arguments);
      return;
    }
    if (arguments[0].matches("value")) {
      commandValue(sender, arguments);
      return;
    }
    throw new WrongUsageException("Type '" + this.getCommandUsage(sender) + "' for help.");
  }
  
  private void commandValue(ICommandSender sender, String[] arguments) {
    EntityPlayerMP player = getPlayerForName(sender.getCommandSenderName());
    if (player != null) {
      ItemStack held = player.getHeldItem();
      if (held != null) {
        double v = QMC.getQMC(held);
        if (v != -1) {
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("Value of ItemStack in your hand is " + v));
        } else {
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("The ItemStack in your hand does not have a value"));
        }
      } else {
        sender.sendChatToPlayer(ChatMessageComponent.createFromText("You have nothing in your hand"));
      }
    }
  }
  
  private void commandHelp(ICommandSender sender, String[] arguments) {
    sender.sendChatToPlayer(ChatMessageComponent.createFromText("Usage"));
    sender.sendChatToPlayer(ChatMessageComponent.createFromText("/oe version -  Returns Open Exchange version"));
  }
  
  private void commandVersion(ICommandSender sender, String[] arguments) {
    sender.sendChatToPlayer(ChatMessageComponent.createFromText(Reference.NAME_DEBUG));
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
  
  public static EntityPlayerMP getPlayerForName(String name) {
    EntityPlayerMP tempPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(name);
    return tempPlayer;
  }
}
