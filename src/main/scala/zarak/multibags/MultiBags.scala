package zarak.multibags

import baubles.api.BaublesApi
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.{Mod, SidedProxy}
import zarak.multibags.api.Reference
import zarak.multibags.client.GuiHandler
import zarak.multibags.init.BagItems
import zarak.multibags.items.ItemBagBase
import zarak.multibags.proxy.CommonProxy

@Mod(modid = Reference.ID, modLanguage = "scala", name = Reference.NAME, dependencies = "required-after:zaraklib;after:jei@[4.8,);")
object MultiBags {

  @SidedProxy(clientSide = "zarak.multibags.proxy.ClientProxy", serverSide = "zarak.multibags.proxy.CommonProxy")
  var proxy: CommonProxy = _

  @EventHandler
  def preInit(event: FMLPreInitializationEvent): Unit = {
    proxy.preInit(event)
  }

  @EventHandler
  def init(event: FMLInitializationEvent): Unit = {
    proxy.init(event)
    NetworkRegistry.INSTANCE.registerGuiHandler(MultiBags, GuiHandler)
  }

  @EventHandler
  def postInit(event: FMLPostInitializationEvent): Unit = {
    proxy.postInit(event)
  }

  lazy val TAB: CreativeTabs = new CreativeTabs(Reference.ID) {
    override def getTabIconItem: ItemStack = {
      new ItemStack(BagItems.ENDER_BAG)
    }
  }

  def hasBagBauble(player: EntityPlayer): Boolean = {
    BaublesApi.getBaublesHandler(player).getStackInSlot(5).getItem.isInstanceOf[ItemBagBase]
  }

  def getBagBauble(player: EntityPlayer): ItemStack = {
    BaublesApi.getBaublesHandler(player).getStackInSlot(5)
  }

  def hasBagHand(player: EntityPlayer, enumHand: EnumHand): Boolean = {
    player.getHeldItem(enumHand).getItem.isInstanceOf[ItemBagBase]
  }

  def getBagHand(player: EntityPlayer, enumHand: EnumHand): ItemStack = {
    player.getHeldItem(enumHand)
  }
}
