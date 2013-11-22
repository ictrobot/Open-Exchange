package oe.lib;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import oe.lib.util.FluidUtil;
import oe.lib.util.ItemStackUtil;
import oe.lib.util.OreDictionaryUtil;
import oe.qmc.QMC;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;

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
    if (arguments[0].toLowerCase().matches("ore")) {
      commandOre(sender, arguments);
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
  
  private void commandFluid(ICommandSender sender, String[] arguments) {
    EntityPlayerMP player = getPlayerForName(sender.getCommandSenderName());
    if (player != null) {
      ItemStack held = player.getHeldItem();
      if (held != null) {
        sender.sendChatToPlayer(ChatMessageComponent.createFromText("--- ItemStack Fluid Data ---"));
        if (FluidUtil.storesFluid(held)) {
          FluidStack f = FluidUtil.getFluidStack(held);
          ItemStack e = FluidUtil.getEmpty(held);
          if (f != null && e != null) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("Fluid:"));
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("  Name: " + FluidUtil.getName(f.fluidID)));
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("  FluidID: " + f.fluidID));
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("  Amount: " + f.amount));
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("  QMC:"));
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("    QMC per 1000mb: " + QMC.getQMC(FluidUtil.getFluid(f.fluidID))));
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("    QMC: " + QMC.getQMC(f)));
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("Empty Container:"));
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("  " + e.getUnlocalizedName() + " (ID:" + e.itemID + " Meta:" + e.getItemDamage() + " QMC:" + QMC.getQMC(e) + ")"));
          }
        } else {
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("The held ItemStack does not store fluid"));
        }
      }
    }
  }
  
  private void commandMod(ICommandSender sender, String[] arguments) {
    EntityPlayerMP player = getPlayerForName(sender.getCommandSenderName());
    if (player != null) {
      ItemStack held = player.getHeldItem();
      if (held != null) {
        GameRegistry.UniqueIdentifier id = null;
        if (ItemStackUtil.isBlock(held.itemID)) {
          Block block = Block.blocksList[held.itemID];
          if (block == null) {
            return;
          }
          id = GameRegistry.findUniqueIdentifierFor(block);
        } else if (ItemStackUtil.isItem(held.itemID)) {
          Item item = Item.itemsList[held.itemID];
          if (item == null) {
            return;
          }
          id = GameRegistry.findUniqueIdentifierFor(item);
        }
        sender.sendChatToPlayer(ChatMessageComponent.createFromText("--- ItemStack Mod Data ---"));
        if (id != null) {
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("Mod: " + id.modId));
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("Name: " + id.name));
        } else {
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("The held ItemStack is not registered correctly in the GameRegistery"));
        }
      }
    }
  }
  
  private void commandOre(ICommandSender sender, String[] arguments) {
    EntityPlayerMP player = getPlayerForName(sender.getCommandSenderName());
    if (player != null) {
      ItemStack held = player.getHeldItem();
      if (held != null) {
        sender.sendChatToPlayer(ChatMessageComponent.createFromText("--- Open Exchange Ore Data ---"));
        int oreID = OreDictionary.getOreID(held);
        if (oreID != -1) {
          String ore = OreDictionary.getOreName(oreID);
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("OreDictionary: " + ore));
          ItemStack[] oreStacks = OreDictionaryUtil.getItemStacks(ore);
          if (oreStacks != null) {
            for (ItemStack stack : oreStacks) {
              sender.sendChatToPlayer(ChatMessageComponent.createFromText("  " + stack.getUnlocalizedName() + " (ID:" + stack.itemID + " Meta:" + stack.getItemDamage() + ")"));
            }
          }
        } else {
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("The held itemstack is not registered in the Ore Dictionary"));
        }
      }
    }
  }
  
  private void commandData(ICommandSender sender, String[] arguments) {
    EntityPlayerMP player = getPlayerForName(sender.getCommandSenderName());
    if (player != null) {
      Log.info(player.username + " queried the database");
      sender.sendChatToPlayer(ChatMessageComponent.createFromText("--- Open Exchange Data ---"));
      ItemStack held = player.getHeldItem();
      if (held != null) {
        if (QMC.hasQMC(held)) {
          sender.sendChatToPlayer(ChatMessageComponent.createFromText(QMC.getQMC(held) + " " + QMC.name));
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("Itemstack " + held.getUnlocalizedName() + " (ID:" + held.itemID + " Meta:" + held.getItemDamage() + ")"));
          int oreID = OreDictionary.getOreID(held);
          if (oreID != -1) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("OreDictionary: " + OreDictionary.getOreName(oreID)));
          }
        } else {
          sender.sendChatToPlayer(ChatMessageComponent.createFromText("The held item does not have a value"));
          sender.sendChatToPlayer(ChatMessageComponent.createFromText(held.getUnlocalizedName() + " (ID:" + held.itemID + " Meta:" + held.getItemDamage() + ")"));
          int oreID = OreDictionary.getOreID(held);
          if (oreID != -1) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("OreDictionary: " + OreDictionary.getOreName(oreID)));
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
    sender.sendChatToPlayer(ChatMessageComponent.createFromText("/oe version - Returns Open Exchange version"));
    sender.sendChatToPlayer(ChatMessageComponent.createFromText("/oe data - Returns info about the item in your hand"));
    sender.sendChatToPlayer(ChatMessageComponent.createFromText("/oe length - Returns the length of the QMC database"));
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
