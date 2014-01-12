package oe;

import java.io.File;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import oe.block.BlockIDs;
import oe.block.Blocks;
import oe.block.gui.GUIHandler;
import oe.block.tile.TileEntities;
import oe.core.CraftingRecipes;
import oe.core.Debug;
import oe.core.Log;
import oe.core.OECommand;
import oe.core.QMCCommand;
import oe.core.Reference;
import oe.core.data.FakePlayer;
import oe.core.data.QuantumToolBlackList;
import oe.core.data.RemoteDrillData;
import oe.core.handler.IMCHandler;
import oe.core.handler.PlayerInteractHandler;
import oe.core.handler.QMCFuelHandler;
import oe.core.handler.ToolTipHandler;
import oe.core.handler.keybind.OEKeyBindHandler;
import oe.core.util.ConfigUtil;
import oe.core.util.OreDictionaryUtil;
import oe.core.util.Util;
import oe.item.ItemIDs;
import oe.item.Items;
import oe.network.connection.ConnectionHandler;
import oe.network.packet.PacketHandler;
import oe.network.proxy.Server;
import oe.qmc.ModIntegration;
import oe.qmc.QMC;
import oe.qmc.guess.CraftingGuessHandlerFactory;
import oe.qmc.guess.FluidGuessHandler;
import oe.qmc.guess.Guess;
import oe.qmc.guess.OreGuessHandler;
import oe.qmc.guess.SmeltingGuessHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
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
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { "OpenExchange", "OpenExchangeTS", "OpenExchangeIM", "OpenExchangeQD", "OpenExchangeBM", "OpenExchangeQMC", "OpenExchangeT" }, packetHandler = PacketHandler.class, connectionHandler = ConnectionHandler.class)
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
    Log.debug("Registering Handlers");
    MinecraftForge.EVENT_BUS.register(new ToolTipHandler());
    MinecraftForge.EVENT_BUS.register(new PlayerInteractHandler());
    Log.debug("Loading Fuel Handler");
    GameRegistry.registerFuelHandler(new QMCFuelHandler());
    Log.debug("Adding QMC Handlers");
    QMC.loadHandlers();
    Log.debug("Adding QMC Guess Handlers");
    Guess.addHandler(new FluidGuessHandler());
    Guess.addHandler(new CraftingGuessHandlerFactory());
    Guess.addHandler(new SmeltingGuessHandler());
    Guess.addHandler(new OreGuessHandler());
    Log.debug("Loading QMC");
    QMC.load();
    if (Util.isClient()) {
      Log.debug("Adding KeyBind Handler");
      KeyBindingRegistry.registerKeyBinding(new OEKeyBindHandler());
    }
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
    Log.debug("Loading Quantum Tool Blacklist");
    QuantumToolBlackList.init();
  }
  
  @EventHandler
  public void load(FMLInitializationEvent event) {
    OreDictionaryUtil.minecraftInit();
    Log.debug("Adding Crafting Recipes");
    CraftingRecipes.load();
  }
  
  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    Log.debug("Loading Mod Integration");
    ModIntegration.init();
    Log.debug("Updating Database Ore Dictionary Values");
    QMC.itemstackHandler.updateOreDictionary();
  }
  
  @EventHandler
  public void handleIMCMessages(IMCEvent event) {
    IMCHandler.processIMCMessages(event);
  }
  
  @EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
    if (ConfigUtil.other("block", "drillRemoteEnabled", true)) {
      RemoteDrillData.loadNBT();
    }
    event.registerServerCommand(new OECommand());
    event.registerServerCommand(new QMCCommand());
  }
  
  @EventHandler
  public void serverStarted(FMLServerStartedEvent event) {
    Log.debug("Loading Fake Player");
    fakePlayer = new FakePlayer.OEFakePlayer();
    QMC.serverStarted();
  }
  
  @EventHandler
  public void serverStop(FMLServerStoppingEvent event) {
    if (ConfigUtil.other("block", "drillRemoteEnabled", true)) {
      RemoteDrillData.saveNBT();
    }
    fakePlayer = null;
  }
}
