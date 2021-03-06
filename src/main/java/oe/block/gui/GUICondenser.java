package oe.block.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import oe.block.container.ContainerCondenser;
import oe.block.tile.TileCondenser;
import oe.qmc.QMC;

public class GUICondenser extends GuiContainer {

  TileCondenser tile;

  public GUICondenser(InventoryPlayer inventoryPlayer, TileCondenser tileEntity) {
    super(new ContainerCondenser(inventoryPlayer, tileEntity));
    tile = tileEntity;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int param1, int param2) {
    // Progress Bar
    GUIHandler.drawProgressPar(tile.percent, this, this.mc);
    // Charging Bench Text
    fontRendererObj.drawString(StatCollector.translateToLocal(tile.getInventoryName()), 26, 3, 4210752);
    // Storage Text
    String text = QMC.formatter.format(tile.stored) + " " + QMC.name;
    int pos = 176 - (text.length() * 6);
    fontRendererObj.drawString(text, pos, 3, 4210752);
    // Inventory Text
    fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, 77, 4210752);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    ResourceLocation Resource = new ResourceLocation("oe:textures/gui/condenser.png");
    this.mc.renderEngine.bindTexture(Resource);
    int x = (width - xSize) / 2;
    int y = (height - ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
  }

}
