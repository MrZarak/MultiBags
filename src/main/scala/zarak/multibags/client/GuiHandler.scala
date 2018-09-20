package zarak.multibags.client

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import zarak.multibags.MultiBags
import zarak.multibags.client.gui.{BagGui, BagUpdateGui}
import zarak.multibags.container.{BagContainer, BagUpdaterContainer, EnderBagContainer, TileBagUpgrader}
import zarak.multibags.items.ItemBagBase

object GuiHandler extends IGuiHandler {

  override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
    ID match {
      case 0 =>
        if (MultiBags.hasBagBauble(player))
          matchBag(player, MultiBags.getBagBauble(player))
        else null

      case 1 => new BagUpdaterContainer(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileBagUpgrader])
      case 2 =>
        if (MultiBags.hasBagHand(player, player.getActiveHand)) {
          val bag = matchBag(player, MultiBags.getBagHand(player,  player.getActiveHand))
          bag.asInstanceOf[BagContainer].setHand(player.getActiveHand)
          bag
        }
        else null
      case _ => null
    }
  }

  def matchBag(player: EntityPlayer, stack: ItemStack): AnyRef = {
    stack.getItem match {
      case bag: ItemBagBase =>
        bag.ID match {
          case -1 => new EnderBagContainer(player.inventory, stack, 45, 9)
          case 0 => new BagContainer(player.inventory, stack, 9, 9)
          case 1 => new BagContainer(player.inventory, stack, 27, 9)
          case 2 => new BagContainer(player.inventory, stack, 45, 9)
          case 3 => new BagContainer(player.inventory, stack, 63, 9)
          case 4 => new BagContainer(player.inventory, stack, 81, 9)
          case _ => null
        }
    }
  }

  @SideOnly(Side.CLIENT)
  override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
    getServerGuiElement(ID, player, world, x, y, z) match {
      case container: BagContainer =>
        container.item.ID match {
          case -1 => new BagGui(508 / 2, 352 / 2, container)
          case 0 => new BagGui(436 / 2, 352 / 2, container)
          case 1 => new BagGui(472 / 2, 352 / 2, container)
          case 2 => new BagGui(508 / 2, 352 / 2, container)
          case 3 => new BagGui(544 / 2, 352 / 2, container)
          case 4 => new BagGui(579 / 2, 352 / 2, container)
          case _ => null
        }
      case bagUpdater: BagUpdaterContainer => new BagUpdateGui(bagUpdater)
      case _ => null
    }
  }
}
