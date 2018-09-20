package zarak.multibags.items

import java.util

import baubles.api.{BaubleType, IBauble}
import com.zarak.zaraklib.item.ItemBase
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.{ActionResult, EnumHand}
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraftforge.items.ItemStackHandler
import zarak.multibags.MultiBags
import zarak.multibags.init.BagItems

import scala.collection.JavaConverters.asScalaSetConverter

class ItemBagBase(name: String, val ID: Int, val size: Int) extends ItemBase(name) with IBauble {
  setMaxStackSize(1)
  setCreativeTab(MultiBags.TAB)
  BagItems.list += this

  override def onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult[ItemStack] = {
    if (!playerIn.isSneaking) {
      playerIn.setActiveHand(handIn)
      if (!worldIn.isRemote) {
        playerIn.openGui(MultiBags, 2, worldIn, 0, 0, 0)
      }
    }

    super.onItemRightClick(worldIn, playerIn, handIn)
  }

  @SideOnly(Side.CLIENT)
  override def addInformation(stack: ItemStack, worldIn: World, tooltip: util.List[String], flagIn: ITooltipFlag): Unit = {
    super.addInformation(stack, worldIn, tooltip, flagIn)
    if (stack.hasTagCompound) {
      val tag = stack.getTagCompound.getCompoundTag("perks")
      tag.getKeySet.asScala.filter(perk => ItemUpdatePerk.perksInfo.contains(perk)).foreach(perk => tooltip.add(ItemUpdatePerk.perksInfo(perk)))
      tooltip.add(s"${TextFormatting.GOLD}${tag.getKeySet.size()}${TextFormatting.AQUA}/${TextFormatting.GOLD}${ItemBagBase.MAX_PERKS}")
    }
  }

  override def getBaubleType(itemStack: ItemStack): BaubleType = {
    BaubleType.BODY
  }

  override def registerItemModel(): Unit = {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("multibags:" + name, "inventory"))
  }

  override def getRGBDurabilityForDisplay(stack: ItemStack): Int = MathHelper.rgb(getDurabilityForDisplay(stack).toFloat / 1.5F, (1.0F - getDurabilityForDisplay(stack).toFloat) / 1.5F, 0.75F)
}


object ItemBagBase {
  val MAX_PERKS = 3

  def loadFromNbt(stack: ItemStack): ItemStackHandler = {
    val handler = new ItemStackHandler()
    stack.getItem match {
      case bag: ItemBagBase =>
        stack.getTagCompound match {
          case tag: NBTTagCompound =>
            handler.deserializeNBT(tag.getCompoundTag("bag_inv"))
          case _ =>
            stack.setTagCompound(new NBTTagCompound)
            handler.setSize(bag.size)
        }
      case _ =>
    }
    handler
  }

  def saveToNbt(stack: ItemStack, handler: ItemStackHandler): Unit = {
    stack.getItem match {
      case _: ItemBagBase =>
        stack.getTagCompound match {
          case tag: NBTTagCompound =>
            tag.setTag("bag_inv", handler.serializeNBT)
          case _ => stack.setTagCompound(new NBTTagCompound)
        }
      case _ =>
    }
  }
}