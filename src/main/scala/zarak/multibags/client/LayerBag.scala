package zarak.multibags.client

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import zarak.multibags.api.Reference
import zarak.multibags.items.ItemBagBase
import zarak.multibags.{BagModel, MultiBags}

class LayerBag extends LayerRenderer[EntityPlayer] {
  override def doRenderLayer(player: EntityPlayer, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float): Unit = {
    val stack: ItemStack = MultiBags.getBagBauble(player)
    stack.getItem match {
      case bag: ItemBagBase =>
        val texture = bag.getRegistryName.getResourcePath + ".png"
        GlStateManager.pushMatrix()
        Minecraft.getMinecraft.getTextureManager.bindTexture(new ResourceLocation(Reference.ID, s"textures/items/$texture"))
        GlStateManager.scale(0.065F, 0.07F, 0.065F)
        GlStateManager.translate(2, 4, 4F)

        GlStateManager.rotate(-180, 0, 1, 0)

        if (player.isSneaking) {
          GlStateManager.rotate(-25, 1, 0, 0)
          GlStateManager.translate(0, 1F, -1)
        }
        new BagModel().render()
        GlStateManager.popMatrix()
      case _ =>
    }
  }

  override def shouldCombineTextures(): Boolean = false
}
