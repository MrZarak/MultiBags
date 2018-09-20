package zarak.multibags.api

import net.minecraft.init.Blocks._
import net.minecraft.init.Items._
import net.minecraft.item.ItemStack
import zarak.multibags.init.BagItems._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks

object BagRecipe {
  val recipes = new mutable.HashMap[ListBuffer[ItemStack], ItemStack]()
  addRecipe(mutable.ListBuffer(
    new ItemStack(FIRST_FABRIC), new ItemStack(CHEST), new ItemStack(FIRST_FABRIC), new ItemStack(FIRST_FABRIC), new ItemStack(CHEST), new ItemStack(FIRST_FABRIC), new ItemStack(FIRST_FABRIC), new ItemStack(FIRST_FABRIC), new ItemStack(FIRST_FABRIC)), new ItemStack(FIRST_BAG))
  addRecipe(mutable.ListBuffer(
    new ItemStack(SECOND_FABRIC), new ItemStack(ENDER_CHEST), new ItemStack(SECOND_FABRIC), new ItemStack(SECOND_FABRIC), new ItemStack(FIRST_BAG), new ItemStack(SECOND_FABRIC), new ItemStack(SECOND_FABRIC), new ItemStack(SECOND_FABRIC), new ItemStack(SECOND_FABRIC)), new ItemStack(SECOND_BAG))
  addRecipe(mutable.ListBuffer(
    new ItemStack(THIRD_FABRIC), new ItemStack(ENDER_CHEST), new ItemStack(THIRD_FABRIC), new ItemStack(THIRD_FABRIC), new ItemStack(SECOND_BAG), new ItemStack(THIRD_FABRIC), new ItemStack(THIRD_FABRIC), new ItemStack(THIRD_FABRIC), new ItemStack(THIRD_FABRIC)), new ItemStack(THIRD_BAG))
  addRecipe(mutable.ListBuffer(
    new ItemStack(FOURTH_FABRIC), new ItemStack(ENDER_CHEST), new ItemStack(FOURTH_FABRIC), new ItemStack(FOURTH_FABRIC), new ItemStack(THIRD_BAG), new ItemStack(FOURTH_FABRIC), new ItemStack(FOURTH_FABRIC), new ItemStack(FOURTH_FABRIC), new ItemStack(FOURTH_FABRIC)), new ItemStack(FOURTH_BAG))
  addRecipe(mutable.ListBuffer(
    new ItemStack(FIFTH_FABRIC), new ItemStack(ENDER_CHEST), new ItemStack(FIFTH_FABRIC), new ItemStack(FIFTH_FABRIC), new ItemStack(FOURTH_BAG), new ItemStack(FIFTH_FABRIC), new ItemStack(FIFTH_FABRIC), new ItemStack(FIFTH_FABRIC), new ItemStack(FIFTH_FABRIC)), new ItemStack(FIFTH_BAG))
  addRecipe(mutable.ListBuffer(
    new ItemStack(ENDER_EYE), new ItemStack(ENDER_CHEST), new ItemStack(ENDER_EYE), new ItemStack(ENDER_CHEST), new ItemStack(THIRD_BAG), new ItemStack(ENDER_CHEST), new ItemStack(ENDER_EYE), new ItemStack(ENDER_CHEST), new ItemStack(ENDER_EYE)), new ItemStack(ENDER_BAG))

  addRecipe(mutable.ListBuffer(
    new ItemStack(TNT), new ItemStack(SECOND_FABRIC), new ItemStack(SECOND_FABRIC), new ItemStack(TNT), new ItemStack(DIAMOND_SWORD), new ItemStack(SECOND_FABRIC), new ItemStack(SECOND_FABRIC), new ItemStack(TNT), new ItemStack(TNT)), new ItemStack(POWER_UPDATE))
  addRecipe(mutable.ListBuffer(
    new ItemStack(PRISMARINE_SHARD), new ItemStack(PRISMARINE_SHARD), new ItemStack(PRISMARINE_SHARD), new ItemStack(FIFTH_FABRIC), new ItemStack(DIAMOND_BOOTS), new ItemStack(FIFTH_FABRIC), new ItemStack(PACKED_ICE), new ItemStack(PRISMARINE_SHARD), new ItemStack(PACKED_ICE)), new ItemStack(SPEED_UPDATE))

  def addRecipe(stacks: ListBuffer[ItemStack], result: ItemStack): Unit = {
    recipes.put(stacks, result)
  }

  def getResult(stacks: ListBuffer[ItemStack]): ItemStack = {
    var stack = ItemStack.EMPTY
    val loop = new Breaks
    for (recipe <- recipes.keySet) {
      if (recipe.length == stacks.size)
        loop.breakable {
          for (i <- recipe.indices)
            if (!isStacksEquals(stacks(i), recipe(i))) loop.break()
          stack = recipes(recipe)
          loop.break()
        }
    }
    stack.copy()
  }

  def isStacksEquals(first: ItemStack, second: ItemStack): Boolean = {
    first.getItem == second.getItem && first.getCount == second.getCount && first.getItemDamage == second.getItemDamage
  }
}
