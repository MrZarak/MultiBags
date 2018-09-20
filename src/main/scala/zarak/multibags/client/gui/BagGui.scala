package zarak.multibags.client.gui

import baubles.client.gui.GuiPlayerExpanded
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.{GuiContainer, GuiInventory}
import net.minecraft.client.gui.{Gui, GuiButton}
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import zarak.multibags.api.Reference
import zarak.multibags.client.EventsClient
import zarak.multibags.container.{BagContainer, SlotBag}
import zarak.multibags.network.{Networking, SPacketOpenGui}

import scala.collection.JavaConverters.collectionAsScalaIterableConverter
import scala.collection.mutable

@SideOnly(Side.CLIENT)
class BagGui(w: Int, h: Int, val container: BagContainer) extends GuiContainer(container) {
  xSize = w
  ySize = h
  override def initGui(): Unit = {
    super.initGui()
      addButton(new GuiButton(0, guiLeft + container.addX + 96, guiTop + container.addY + 6, 16, 16, "") {


        override def mousePressed(mc: Minecraft, mouseX: Int, mouseY: Int): Boolean = {
          if (isMouseOver)
            Networking.sendToServer(new SPacketOpenGui("bauble"))
          isMouseOver
        }

        override def drawButton(mc: Minecraft, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
          if (this.visible) {
            GlStateManager.pushMatrix()
            mc.getTextureManager.bindTexture(GuiPlayerExpanded.background)
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F)
            GlStateManager.enableBlend()
            GlStateManager.translate(0.0F, 0.0F, 200.0F)
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height
            if (isMouseOver) {
              this.drawTexturedModalRect(x, y, 210, 48, 10, 10)
              drawHoveringText("Baubles", mouseX, mouseY)
            } else
              this.drawTexturedModalRect(x, y, 200, 48, 10, 10)
            GlStateManager.disableBlend()
            GlStateManager.popMatrix()
          }
        }
      })
      addButton(new GuiButton(1, guiLeft + container.addX + 187, guiTop + container.addY + 2, 16, 16, "") {
        var alpha = 0.0F

        override def mousePressed(mc: Minecraft, mouseX: Int, mouseY: Int): Boolean = {
          if (isMouseOver) {
            EventsClient.canVanillaOpen = true
            mc.displayGuiScreen(new GuiInventory(mc.player))
          }
          isMouseOver
        }

        override def drawButton(mc: Minecraft, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
          if (this.visible) {
            GlStateManager.pushMatrix()
            mc.getTextureManager.bindTexture(BagGui.inv)

            GlStateManager.enableBlend()
            GlStateManager.translate(0.0F, 0.0F, 200.0F)
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height
            if (isMouseOver)
              alpha += 0.02F
            else
              alpha -= 0.02F
            alpha = math.max(math.min(alpha, 1F), 0.3F)
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha)
            Gui.drawScaledCustomSizeModalRect(x, y, 0, 0, 32, 32, 16, 16, 32, 32)
            if (alpha >= 0.8)
              drawHoveringText(I18n.format("gui_bag_open_vanilla_gui"), mouseX - 80, mouseY - 5)
            GlStateManager.popMatrix()
          }
        }
      })
  }


  override def drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int): Unit = {
    GlStateManager.pushMatrix()
    GlStateManager.color(1, 1, 1, 1)
    mc.getTextureManager.bindTexture(BagGui.getIcon(container.item.ID))
    Gui.drawScaledCustomSizeModalRect(guiLeft, guiTop, 0, 0, xSize * 2, ySize * 2, xSize, ySize, 768, 768)
    GlStateManager.popMatrix()
  }

  override def drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    if (container.render) {
      drawDefaultBackground()
      super.drawScreen(mouseX, mouseY, partialTicks)
      renderHoveredToolTip(mouseX, mouseY)
    }
  }

  override def drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int): Unit = {
    mc.getTextureManager.bindTexture(BagGui.cross)
    inventorySlots.inventorySlots.asScala.filter(slot => slot.isInstanceOf[SlotBag] && !slot.isItemValid(mc.player.inventory.getItemStack)).foreach(slot => {
      val x = slot.xPos
      val y = slot.yPos
      Gui.drawScaledCustomSizeModalRect(x + 1, y + 1, 0, 0, 14, 14, 14, 14, 14, 14)
    })
    GuiInventory.drawEntityOnScreen(container.addX + 83, container.addY + 70, 32, 80 - mouseX, 80 - mouseY, this.mc.player)
  }
}

object BagGui {
  final val icons = new mutable.HashMap[Int, ResourceLocation]()
  final val cross = new ResourceLocation(Reference.ID, "textures/gui/cross.png")
  final val inv = new ResourceLocation(Reference.ID, "textures/gui/inv.png")

  def getIcon(id: Int): ResourceLocation = icons.getOrElse(id, new ResourceLocation(""))

  def registerBackground(ID: Int, location: ResourceLocation): Unit = icons.put(ID, location)
}
