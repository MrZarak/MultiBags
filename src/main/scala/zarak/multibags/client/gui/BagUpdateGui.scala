package zarak.multibags.client.gui


import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import zarak.multibags.api.Reference
import zarak.multibags.client.gui.BagUpdateGui._
import zarak.multibags.container.BagUpdaterContainer
@SideOnly(Side.CLIENT)
class BagUpdateGui(container: BagUpdaterContainer) extends GuiContainer(container) {

  override def drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int): Unit = {
    mc.getTextureManager.bindTexture(BG)
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
  }

  override def drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int): Unit = {
    val i = Math.round(this.container.tile.progress / 100F * 16F)
    GlStateManager.pushMatrix()
    Minecraft.getMinecraft.getTextureManager.bindTexture(BG)
    drawTexturedModalRect(127, 33, 176, 5, i, 15)
    GlStateManager.popMatrix()
  }

  override def drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    drawDefaultBackground()
    super.drawScreen(mouseX, mouseY, partialTicks)
    renderHoveredToolTip(mouseX, mouseY)
  }
}

object BagUpdateGui {
  val BG = new ResourceLocation(Reference.ID, "textures/gui/bag_repair.png")
}

