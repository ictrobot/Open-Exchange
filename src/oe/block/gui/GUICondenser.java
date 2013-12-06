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
    ResourceLocation bar_texture = new ResourceLocation("oe:textures/gui/condenser_progress_bar.png");
    this.mc.renderEngine.bindTexture(bar_texture);
    int per = 0;
    if (tile.hasTarget) {
      per = tile.percent;
      if (per > 100) {
        per = 100;
      }
    } else {
      per = 100;
    }
    per = per + 3;
    this.drawTexturedModalRect(35, 9, 0, 20, per, 12);
    this.drawTexturedModalRect(35, 9, 0, 0, 106, 12);
    // Charging Bench Text
    fontRenderer.drawString(StatCollector.translateToLocal(tile.getInvName()), 26, 3, 4210752);
    // Storage Text
    String text = QMC.formatter.format(tile.stored) + " " + QMC.name;
    int pos = 176 - (text.length() * 6);
    fontRenderer.drawString(text, pos, 3, 4210752);
    // Inventory Text
    fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 77, 4210752);
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
