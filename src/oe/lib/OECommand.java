package oe.lib;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.oredict.OreDictionary;
import oe.qmc.QMC;
import oe.qmc.QMCData;
import oe.qmc.QMCType;
import cpw.mods.fml.common.FMLCommonHandler;

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
    if (arguments[0].toLowerCase().matches("version")) {
      commandVersion(sender, arguments);
      return;
    }
    if (arguments[0].toLowerCase().matches("help")) {
      commandHelp(sender, arguments);
      return;
    }
    if (arguments[0].toLowerCase().matches(QMC.name.toLowerCase())) {
      commandValue(sender, arguments);
      return;
    }
    if (arguments[0].toLowerCase().matches("data")) {
      commandData(sender, arguments);
      return;
    }
    if (arguments[0].toLowerCase().matches("length")) {
      commandLength(sender, arguments);
      return;
    }
    throw new WrongUsageException("Type '" + this.getCommandUsage(sender) + "' for help.");
  }
  
  private void commandLength(ICommandSender sender, String[] arguments) {
    EntityPlayerMP player = getPlayerForName(sender.getCommandSenderName());
    if (player != null) {
      sender.sendChatToPlayer(ChatMessageComponent.createFromText("The length of the " + QMC.nameFull + " database is " + QMC.length()));
    }
  }
  
  private void commandData(ICommandSender sender, String[] arguments) {
    EntityPlayerMP player = getPlayerForName(sender.getCommandSenderName());
    if (player != null) {
      Log.info(player.username + " queried the database");
      sender.sendChatToPlayer(ChatMessageComponent.createFromText("--- Open Exchange Data ---"));
      ItemStack held = player.getHeldItem();
      if (held != null) {
        int ref = QMC.getReference(held);
        QMCData d = QMC.getQMCData(ref);
        if (d != null) {
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("Reference: " + ref + " - " + d.QMC + " " + QMC.name + " - " + d.type));
          if (d.type != QMCType.OreDictionary) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("Itemstack " + d.itemstack.getUnlocalizedName() + " (ID:" + d.itemstack.itemID + " Meta:" + held.getItemDamage() + ")"));
          }
          if (d.type != QMCType.Itemstack) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("OreDictionary " + d.oreDictionary));
          }
          if (d.guess != null) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("Value Guessed:"));
            for (int i = 0; i < d.guess.ingredients.length; i++) {
              if (d.guess.ingredients[i] != null) {
                sender.sendChatToPlayer(ChatMessageComponent.createFromText("  " + i + ": " + d.guess.ingredients[i].getUnlocalizedName() + " (ID:" + d.guess.ingredients[i].itemID + " Meta:" + held.getItemDamage() + ") " + QMC.name + ": " + d.guess.ingredientsQMC[i]));
              }
            }
            if (d.guess.outputNum > 1) {
              sender.sendChatToPlayer(ChatMessageComponent.createFromText("Makes " + d.guess.outputNum));
            }
          }
        } else {
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("The held item does not have a value"));
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("ID: " + held.itemID + " Meta: " + held.getItemDamage()));
          int oreID = OreDictionary.getOreID(held);
          if (oreID != -1) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("OreDictionary: " + OreDictionary.getOreName(oreID)));
            if (QMC.hasValue(OreDictionary.getOreName(oreID))) {
              sender.sendChatToPlayer(ChatMessageComponent.createFromText("However the database value for " + OreDictionary.getOreName(oreID) + " is " + QMC.getQMC(OreDictionary.getOreName(oreID))));
            }
          }
        }
      }
    }
  }
  
  private void commandValue(ICommandSender sender, String[] arguments) {
    EntityPlayerMP player = getPlayerForName(sender.getCommandSenderName());
    if (player != null) {
      ItemStack held = player.getHeldItem();
      if (held != null) {
        double v = QMC.getQMC(held);
        if (v != -1) {
          sender.sendChatToPlayer(ChatMessageComponent.createFromText(QMC.name + " value of ItemStack in your hand is " + v));
        } else {
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("The ItemStack in your hand does not have a " + QMC.name + " value"));
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
