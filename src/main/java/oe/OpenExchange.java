package oe;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import oe.block.OEBlocks;
import oe.block.gui.GUIHandler;
import oe.block.tile.TileEntities;
import oe.core.*;
import oe.core.data.OEFakePlayer;
import oe.core.data.QuantumToolBlackList;
import oe.core.data.TileSync;
import oe.core.handler.EntityJoinHandler;
import oe.core.handler.IMCHandler;
import oe.core.handler.QMCFuelHandler;
import oe.core.handler.ToolTipHandler;
import oe.core.handler.keybind.OEKeyBindHandler;
import oe.core.util.ConfigUtil;
import oe.core.util.Util;
import oe.item.OEItems;
import oe.network.Networking;
import oe.network.proxy.Server;
import oe.qmc.ModIntegration;
import oe.qmc.QMC;
import oe.qmc.QMCItemStack;
import oe.qmc.guess.*;

import java.io.File;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER)
public class OpenExchange {

  public static File configdir;

  public static boolean debug; // Debug Enabled

  @SidedProxy(clientSide = "oe.network.proxy.Client", serverSide = "oe.network.proxy.Server")
  public static Server proxy;

  public static EntityPlayerMP fakePlayer;

  @Instance("OE")
  public static OpenExchange instance;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    configdir = event.getModConfigurationDirectory();
    debug = ConfigUtil.other("DEBUG", "Debug enabled", Debug.rawCode);
    if (debug) {
      Log.info("Debugging Enabled");
    }
    Log.debug("Loading Fuel Handler");
    GameRegistry.registerFuelHandler(new QMCFuelHandler());
    Log.debug("Adding QMC Handlers");
    QMC.loadHandlers();
    Log.debug("Adding QMC Guess Factories");
    Guess.addFactory(new SimpleGuessHandlerFactory(new FluidGuessHandler(SimpleGuessHandlerFactory.class)));
    Guess.addFactory(new CraftingGuessHandlerFactory());
    Guess.addFactory(new SmeltingGuessHandlerFactory());
    Guess.addFactory(new SimpleGuessHandlerFactory(new OreGuessHandler(SimpleGuessHandlerFactory.class)));
    Log.debug("Loading QMC");
    QMC.load();
    Log.debug("Loading Blocks");
    OEBlocks.load();
    Log.debug("Loading Items");
    OEItems.load();
    Log.debug("Registering Handlers");
    MinecraftForge.EVENT_BUS.register(new ToolTipHandler());
    MinecraftForge.EVENT_BUS.register(OEItems.blockManipulator);
    MinecraftForge.EVENT_BUS.register(new TileSync());
    MinecraftForge.EVENT_BUS.register(new EntityJoinHandler());
    if (Util.isClient()) {
      Log.debug("Adding KeyBind Handler");
      MinecraftForge.EVENT_BUS.register(new OEKeyBindHandler());
    }
    Log.debug("Registering Blocks");
    OEBlocks.register();
    Log.debug("Registering Items");
    OEItems.register();
    Log.debug("Registering Tile Entities");
    TileEntities.Register();
    Log.debug("Registering GUI Handler");
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());
    Log.debug("Loading Quantum Tool Blacklist");
    QuantumToolBlackList.init();
    Log.debug("Initialising Networking");
    Networking.preInit();
  }

  @EventHandler
  public void load(FMLInitializationEvent event) {
    Log.debug("Adding Crafting Recipes");
    CraftingRecipes.load();
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    Log.debug("Loading Mod Integration");
    ModIntegration.init();
    Log.debug("Printing QMC Database");
    Log.debug(QMC.databaseString());
  }

  @EventHandler
  public void handleIMCMessages(IMCEvent event) {
    IMCHandler.processIMCMessages(event);
  }

  private int starts = 0;

  @EventHandler
  public void serverAboutToStart(FMLServerAboutToStartEvent event) {
    if (starts == 0) {
      QMC.process(QMCItemStack.instance);
    }
    starts++;
  }

  @EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
    event.registerServerCommand(new OECommand());
    event.registerServerCommand(new QMCCommand());
  }

  @EventHandler
  public void serverStarted(FMLServerStartedEvent event) {
    Log.debug("Loading Fake Player");
    fakePlayer = new OEFakePlayer();
    QMC.serverStarted();
  }

  @EventHandler
  public void serverStop(FMLServerStoppingEvent event) {
    fakePlayer = null;
  }
}
