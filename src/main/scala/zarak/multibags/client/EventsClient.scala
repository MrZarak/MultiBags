package zarak.multibags.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.client.resources.I18n
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.relauncher.Side
import org.lwjgl.input.Keyboard
import zarak.multibags.MultiBags
import zarak.multibags.api.Reference
import zarak.multibags.network.{Networking, SPacketOpenGui}

@EventBusSubscriber(modid = Reference.ID, value = Array(Side.CLIENT))
object EventsClient {
  val BAG_KEY = new KeyBinding(I18n.format("key.open_gui"), Keyboard.KEY_G, I18n.format("key.category_bag"))
  ClientRegistry.registerKeyBinding(BAG_KEY)
  var canVanillaOpen = false

  @SubscribeEvent
  def press(event: InputEvent.KeyInputEvent): Unit = {
    if (Keyboard.isKeyDown(BAG_KEY.getKeyCode))
      Networking.sendToServer(new SPacketOpenGui("bag"))

  }

  @SubscribeEvent
  def open(event: GuiOpenEvent): Unit = {
    val mc = Minecraft.getMinecraft
    if (mc.player != null)
      if (MultiBags.hasBagBauble(mc.player)) {
        event.getGui match {
          case _: GuiInventory =>
            if (!mc.player.isCreative)
              if (!canVanillaOpen) {
                event.setCanceled(true)
                Networking.sendToServer(new SPacketOpenGui("bag"))
              }
            canVanillaOpen = false
          case _ =>
        }
      }

  }
}
