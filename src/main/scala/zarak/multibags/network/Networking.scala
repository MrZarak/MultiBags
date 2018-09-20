package zarak.multibags.network

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side
import zarak.multibags.api.Reference

object Networking extends SimpleNetworkWrapper(Reference.ID) {
  this.registerMessage(classOf[SPacketOpenGui], classOf[SPacketOpenGui], 0, Side.SERVER)
  this.registerMessage(classOf[SPacketSyncSlot], classOf[SPacketSyncSlot], 1, Side.SERVER)
}
