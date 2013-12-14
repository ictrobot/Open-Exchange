package oe.core;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatMessageComponent;
import oe.qmc.QMC;
import cpw.mods.fml.common.FMLCommonHandler;

public class QMCCommand extends CommandBase {
  @SuppressWarnings("rawtypes")
  private List aliases;
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public QMCCommand() {
    this.aliases = new ArrayList();
    this.aliases.add(QMC.name.toLowerCase());
    this.aliases.add(QMC.name.toUpperCase());
  }
  
  @Override
  public String getCommandName() {
    return QMC.name.toUpperCase();
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
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  @Override
  public void processCommand(ICommandSender sender, String[] arguments) {
    if (arguments.length <= 0) {
      throw new WrongUsageException("Type '" + this.getCommandUsage(sender) + "' for help.");
    }
    if (arguments[0].toLowerCase().matches("save")) {
      commandSave(sender, arguments);
      return;
    }
    if (arguments[0].toLowerCase().matches("help")) {
      commandHelp(sender, arguments);
      return;
    }
    throw new WrongUsageException("Type '" + this.getCommandUsage(sender) + "' for help.");
  }
  
  private void commandSave(ICommandSender sender, String[] arguments) {
    QMC.regenerateSave("Save Command from " + sender.getCommandSenderName());
  }
  
  private void commandHelp(ICommandSender sender, String[] arguments) {
    sender.sendChatToPlayer(ChatMessageComponent.createFromText("Usage"));
    sender.sendChatToPlayer(ChatMessageComponent.createFromText("/QMC save - Rebuilds QMCSave"));
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