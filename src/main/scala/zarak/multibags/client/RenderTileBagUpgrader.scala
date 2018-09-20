package zarak.multibags.client

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import zarak.multibags.container.TileBagUpgrader

class RenderTileBagUpgrader extends TileEntitySpecialRenderer[TileBagUpgrader] {
  override def render(te: TileBagUpgrader, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float): Unit = {
    val mc = Minecraft.getMinecraft
    val inv = te.inventory
    for (i <- 0 until 9) {
      val stackIngr = inv.getStackInSlot(i + 1)
      if (!stackIngr.isEmpty) {
        GlStateManager.pushMatrix()
        GlStateManager.translate(x + i % 3 * 0.21D + 0.29D, y + 0.45D, z + i / 3 * 0.21D + 0.29D)
        GlStateManager.scale(0.3F, 0.3F, 0.3F)
        GlStateManager.rotate(mc.world.getTotalWorldTime, 0, 1, 0)
        mc.getItemRenderer.renderItem(mc.player, stackIngr, ItemCameraTransforms.TransformType.GROUND)
        GlStateManager.popMatrix()
      }
    }
    val stack = if(inv.getStackInSlot(12).isEmpty) inv.getStackInSlot(10) else inv.getStackInSlot(12)
    if (!stack.isEmpty) {
      GlStateManager.pushMatrix()
      val scale: Float = math.abs(math.cos(((mc.world.getTotalWorldTime + partialTicks) / 7D) / 4D).toFloat)
      GlStateManager.translate(x + 0.5D, y + 1- scale / 5F, z + 0.5D)
      GlStateManager.rotate(mc.world.getTotalWorldTime * 5F, 0, 1, 0)
      GlStateManager.scale(scale, scale, scale)
      mc.getItemRenderer.renderItem(mc.player, stack, ItemCameraTransforms.TransformType.GROUND)
      if (!inv.getStackInSlot(11).isEmpty) {
        GlStateManager.scale(scale/4D, scale/4D, scale/4D)
        GlStateManager.translate(math.sin((mc.world.getTotalWorldTime + partialTicks) / 6D), 0, math.cos((mc.world.getTotalWorldTime + partialTicks) / 6D))
        mc.getItemRenderer.renderItem(mc.player, inv.getStackInSlot(11), ItemCameraTransforms.TransformType.GROUND)
      }
      GlStateManager.popMatrix()
    }
  }
}
