package zarak.multibags.client

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.entity.player.EntityPlayer

object Layers {
  setLayer(new LayerBag)

  def setLayer(layer: LayerRenderer[EntityPlayer]): Unit = {
    Minecraft.getMinecraft.getRenderManager.getSkinMap.get("default").addLayer[EntityPlayer,LayerRenderer[EntityPlayer]](layer)
    Minecraft.getMinecraft.getRenderManager.getSkinMap.get("slim").addLayer[EntityPlayer,LayerRenderer[EntityPlayer]](layer)
  }

}
