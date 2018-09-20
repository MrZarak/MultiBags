package zarak.multibags.network

import baubles.api.BaublesApi
import io.netty.buffer.ByteBuf
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}

import scala.collection.JavaConverters.collectionAsScalaIterableConverter


class SPacketSyncSlot(var windowID: Int, var slotID: Int, var stack: ItemStack) extends IMessage with IMessageHandler[SPacketSyncSlot, SPacketSyncSlot] {
  def this() {
    this(0, 0, ItemStack.EMPTY)
  }

  def fromBytes(buf: ByteBuf): Unit = {
    windowID = buf.readInt()
    slotID = buf.readInt()
    stack = ByteBufUtils.readItemStack(buf)
  }

  def toBytes(buf: ByteBuf): Unit = {
    buf.writeInt(windowID)
    buf.writeInt(slotID)
    ByteBufUtils.writeItemStack(buf, stack)
  }

  override def onMessage(message: SPacketSyncSlot, ctx: MessageContext): SPacketSyncSlot = {
    val player = ctx.getServerHandler.player
    if( player.openContainer.windowId == message.slotID)
      player.openContainer.inventorySlots.asScala.toList(message.slotID).putStack(message.stack)
    else message.windowID match {
      case SPacketSyncSlot.BAUBLE => BaublesApi.getBaublesHandler(player).setStackInSlot(message.slotID, message.stack)
      case _ =>
    }
    null
  }
}

object SPacketSyncSlot {
  val BAUBLE: Int = -7
}