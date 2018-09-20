package zarak.multibags.blocks

import com.zarak.zaraklib.block.BlockBase
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.state.{BlockFaceShape, IBlockState}
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.client.model.ModelLoader
import zarak.multibags.MultiBags
import zarak.multibags.api.Reference
import zarak.multibags.container.TileBagUpgrader
import zarak.multibags.init.BagBlocks

class BlockBagUpgrader(name: String) extends BlockBase(name) with ITileEntityProvider {
  setCreativeTab(MultiBags.TAB)
  BagBlocks.list +=this

  override def getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB = {
    new AxisAlignedBB(0.1F,0,0.1F,0.9F,0.42F,0.9F)
  }
  override def onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if (!worldIn.isRemote) {
      playerIn.openGui(MultiBags, 1, worldIn, pos.getX, pos.getY, pos.getZ)
    }
    true
  }

  override def isOpaqueCube(state: IBlockState): Boolean = false

  override def getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing): BlockFaceShape = BlockFaceShape.UNDEFINED

  override def createNewTileEntity(worldIn: World, meta: Int): TileEntity = {
    new TileBagUpgrader
  }

  override def registerItemModel(itemBlock: Item): Unit = {
    ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(s"${Reference.ID}:" + name, "inventory"))
  }
}
