package oe;

import java.io.File;
import oe.handler.IMC;
import oe.helper.ConfigHelper;
import oe.item.ItemIDs;
import oe.item.Items;
import net.minecraftforge.common.MinecraftForge;
import oe.block.BlockIDs;
import oe.block.Blocks;
import oe.block.gui.GUIHandler;
import oe.block.tile.TileEntities;
import oe.client.ToolTip;
import oe.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import oe.packet.*;
import oe.qmc.QMC;
import oe.qmc.guess.Crafting;
import oe.qmc.guess.Guess;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { "oe" }, packetHandler = PacketHandler.class)
public class OpenExchange {
  
  public static File configdir;
  
  public static boolean debug; // Debug Enabled
  public static boolean guess; // Guessing of values enabled?
  
  @SidedProxy(clientSide = "oe.proxy.ClientProxy", serverSide = "oe.proxy.CommonProxy")
  public static CommonProxy proxy;
  
  @Instance("OE")
  public static OpenExchange instance;
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    configdir = event.getModConfigurationDirectory();
    ConfigHelper.load();
    debug = ConfigHelper.other("DEBUG", "Debug enabled", false);
    guess = ConfigHelper.other("Guessing", "Try to Guess values?", true);
    ConfigHelper.save();
    Log.debug("Loading Exchange Values");
    QMC.load();
    Log.debug("Loading Block IDs");
    BlockIDs.Load();
    Log.debug("Loading Blocks");
    Blocks.Load();
    Log.debug("Loading Item IDs");
    ItemIDs.Load();
    Log.debug("Loading Items");
    Items.Load();
    Log.debug("Registering Blocks");
    Blocks.Register();
    Log.debug("Registering Items");
    Items.Register();
    Log.debug("Registering Tile Entities");
    TileEntities.Register();
    Log.debug("Registering GUI Handler");
    NetworkRegistry.instance().registerGuiHandler(OpenExchange.instance, new GUIHandler());
    if (guess) {
      Log.debug("Adding QMC Guessers");
      Guess.add(Crafting.class);
    }
  }
  
  @EventHandler
  public void load(FMLInitializationEvent event) {
  }
  
  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(new ToolTip());
  }
  
  @EventHandler
  public void handleIMCMessages(IMCEvent event) {
    IMC.processIMCMessages(event);
  }
  
  @EventHandler
  public void serverLoad(FMLServerStartingEvent event) {
    if (guess) { // Loads at server start to make sure all recipes are registered
      Log.debug("Guessing QMC Values");
      Guess.load();
    }
    event.registerServerCommand(new OECommand());
  }
}
