package zarak.multibags.network

import baubles.common.Baubles
import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}
import zarak.multibags.MultiBags


class SPacketOpenGui(var name: String) extends IMessage with IMessageHandler[SPacketOpenGui, SPacketOpenGui] {
  def this(){
    this("")
  }

  def fromBytes(buf: ByteBuf): Unit = {
    name = ByteBufUtils.readUTF8String(buf)
  }

  def toBytes(buf: ByteBuf): Unit = {
    ByteBufUtils.writeUTF8String(buf, name)
  }

  override def onMessage(message: SPacketOpenGui, ctx: MessageContext): SPacketOpenGui = {
    message.name.toLowerCase match {
      case "bag" => ctx.getServerHandler.player.openGui(MultiBags, 0, ctx.getServerHandler.player.world, 0, 0, 0)
      case "bauble" => ctx.getServerHandler.player.openGui(Baubles.instance, 0, ctx.getServerHandler.player.world, 0, 0, 0)
      case _ =>
    }
    null
  }
}