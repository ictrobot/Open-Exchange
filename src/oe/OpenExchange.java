package oe;

import java.io.File;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import oe.block.BlockIDs;
import oe.block.Blocks;
import oe.block.gui.GUIHandler;
import oe.block.tile.TileEntities;
import oe.core.CraftingRecipes;
import oe.core.Log;
import oe.core.OECommand;
import oe.core.Reference;
import oe.core.data.FakePlayer;
import oe.core.data.QuantumToolBlackList;
import oe.core.data.RemoteDrillData;
import oe.core.handler.IMCHandler;
import oe.core.handler.PlayerInteractHandler;
import oe.core.handler.PlayerTracker;
import oe.core.handler.QMCFuelHandler;
import oe.core.handler.ToolTipHandler;
import oe.core.util.ConfigUtil;
import oe.core.util.OreDictionaryUtil;
import oe.item.ItemIDs;
import oe.item.Items;
import oe.network.packet.PacketHandler;
import oe.network.proxy.Server;
import oe.qmc.ModIntegration;
import oe.qmc.QMC;
import oe.qmc.guess.CraftingGuessHandler;
import oe.qmc.guess.FluidGuessHandler;
import oe.qmc.guess.Guess;
import oe.qmc.guess.SmeltingGuessHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { "oe", "oeQD", "oeBM", "oeQMC", "oeQMCReset", "oeTileInfo" }, packetHandler = PacketHandler.class)
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
    ConfigUtil.load();
    debug = ConfigUtil.other("DEBUG", "Debug enabled", false);
    boolean fuelHandler = ConfigUtil.other("FuelHandler", "Allow any item to be burnt if it has a QMC value", false);
    ConfigUtil.save();
    if (debug) {
      Log.info("Debugging Enabled");
    }
    Log.debug("Registering Handlers");
    MinecraftForge.EVENT_BUS.register(new ToolTipHandler());
    MinecraftForge.EVENT_BUS.register(new PlayerInteractHandler());
    GameRegistry.registerPlayerTracker(new PlayerTracker());
    Log.debug("Adding QMC Handlers");
    QMC.loadHandlers();
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
    if (fuelHandler) {
      Log.debug("Loading Fuel Handler");
      GameRegistry.registerFuelHandler(new QMCFuelHandler());
    }
    Log.debug("Loading Quantum Tool Blacklist");
    QuantumToolBlackList.init();
  }
  
  @EventHandler
  public void load(FMLInitializationEvent event) {
    OreDictionaryUtil.minecraftInit();
    Log.debug("Loading QMC Values");
    QMC.load();
    Log.debug("Adding QMC Guessers");
    Guess.addHandler(new FluidGuessHandler());
    Guess.addHandler(new CraftingGuessHandler());
    Guess.addHandler(new SmeltingGuessHandler());
    Log.debug("Adding Crafting Recipes");
    CraftingRecipes.load();
  }
  
  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    Log.debug("Loading Mod Integration");
    ModIntegration.init();
    Log.debug("Updating Database Ore Dictionary Values");
    QMC.itemstackHandler.updateOreDictionary();
    QMC.takePostInitSnapshot();
  }
  
  @EventHandler
  public void handleIMCMessages(IMCEvent event) {
    IMCHandler.processIMCMessages(event);
  }
  
  @EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
    if (ConfigUtil.other("block", "drillRemoteEnabled", true)) {
      event.registerServerCommand(new OECommand());
    }
    RemoteDrillData.loadNBT();
  }
  
  @EventHandler
  public void serverStarted(FMLServerStartedEvent event) {
    QMC.restoreSnapshot(QMC.postInitSnapshot);
    Log.debug("Loading Fake Player");
    fakePlayer = new FakePlayer.OEFakePlayer();
    Log.debug("Guessing QMC Values");
    Guess.load();
  }
  
  @EventHandler
  public void serverStop(FMLServerStoppingEvent event) {
    if (ConfigUtil.other("block", "drillRemoteEnabled", true)) {
      RemoteDrillData.saveNBT();
    }
    fakePlayer = null;
  }
  
  @EventHandler
  public void serverStopped(FMLServerStoppedEvent event) {
    if (proxy.isClient()) {
      QMC.restoreSnapshot(QMC.postInitSnapshot);
    }
  }
}
