package oe;

import java.io.File;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import oe.block.BlockIDs;
import oe.block.Blocks;
import oe.block.gui.GUIHandler;
import oe.block.tile.TileEntities;
import oe.item.ItemIDs;
import oe.item.Items;
import oe.lib.CraftingRecipes;
import oe.lib.FakePlayer;
import oe.lib.Log;
import oe.lib.OECommand;
import oe.lib.QuantumToolBlackList;
import oe.lib.Reference;
import oe.lib.RemoteDrillData;
import oe.lib.handler.IMCHandler;
import oe.lib.handler.PlayerInteractHandler;
import oe.lib.handler.PlayerTracker;
import oe.lib.handler.QMCFuelHandler;
import oe.lib.handler.ToolTipHandler;
import oe.lib.handler.ore.OreDictionaryHelper;
import oe.lib.handler.ore.OreDictionaryWriter;
import oe.lib.helper.ConfigHelper;
import oe.network.packet.PacketHandler;
import oe.network.proxy.Server;
import oe.qmc.ModIntegration;
import oe.qmc.QMC;
import oe.qmc.file.QMCValuesWriter;
import oe.qmc.guess.Crafting;
import oe.qmc.guess.Guess;
import oe.qmc.guess.Ore;
import oe.qmc.guess.Smelting;
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

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { "oe", "oeQD", "oeTransmutation", "oeBM", "oeQMC", "oeQMCWipe" }, packetHandler = PacketHandler.class)
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
    ConfigHelper.load();
    debug = ConfigHelper.other("DEBUG", "Debug enabled", false);
    boolean fuelHandler = ConfigHelper.other("FuelHandler", "Allow any item to be burnt if it has a QMC value", false);
    ConfigHelper.save();
    if (debug) {
      Log.info("Debugging Enabled");
    }
    Log.debug("Registering Handlers");
    MinecraftForge.EVENT_BUS.register(new ToolTipHandler());
    MinecraftForge.EVENT_BUS.register(new OreDictionaryHelper());
    MinecraftForge.EVENT_BUS.register(new PlayerInteractHandler());
    GameRegistry.registerPlayerTracker(new PlayerTracker());
    Log.debug("Loading QMC Values");
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
    Log.debug("Adding QMC Guessers");
    Guess.add(Crafting.class);
    Guess.add(Smelting.class);
    Guess.add(Ore.class);
    if (fuelHandler) {
      Log.debug("Loading Fuel Handler");
      GameRegistry.registerFuelHandler(new QMCFuelHandler());
    }
    Log.debug("Loading Quantum Tool Blacklist");
    QuantumToolBlackList.init();
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
    Log.debug("Updating Database Ore Dictionary Values");
    QMC.updateOreDictionary();
  }
  
  @EventHandler
  public void handleIMCMessages(IMCEvent event) {
    IMCHandler.processIMCMessages(event);
  }
  
  @EventHandler
  public void serverLoad(FMLServerStartingEvent event) {
    if (ConfigHelper.other("block", "drillRemoteEnabled", true)) {
      event.registerServerCommand(new OECommand());
    }
    RemoteDrillData.loadNBT();
  }
  
  @EventHandler
  public void serverStarted(FMLServerStartedEvent event) {
    Log.debug("Loading Fake Player");
    fakePlayer = new FakePlayer.OEFakePlayer();
    Log.debug("Guessing QMC Values");
    Guess.load();
    Log.debug("Writing QMC Values to a file");
    QMCValuesWriter.write();
    Log.debug("Writing OreDictionary Values to a file");
    OreDictionaryWriter.write();
  }
  
  @EventHandler
  public void serverStop(FMLServerStoppingEvent event) {
    if (ConfigHelper.other("block", "drillRemoteEnabled", true)) {
      RemoteDrillData.saveNBT();
    }
    fakePlayer = null;
  }
}
