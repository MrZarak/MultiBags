package zarak.multibags.jei

import mezz.jei.api.recipe.IRecipeCategoryRegistration
import mezz.jei.api.{IModPlugin, IModRegistry, JEIPlugin}
import net.minecraft.item.ItemStack
import zarak.multibags.api.BagRecipe
import zarak.multibags.init.{BagBlocks, BagItems}
import zarak.multibags.items.{ItemBagBase, ItemUpdateBase}

import scala.collection.JavaConverters.asJavaCollectionConverter
import scala.collection.mutable.ListBuffer

@JEIPlugin
class JeiMain extends IModPlugin {

  override def registerCategories(registry: IRecipeCategoryRegistration): Unit = {
    registry.addRecipeCategories(new BagInfuserCategory(registry.getJeiHelpers.getGuiHelper))
    registry.addRecipeCategories(new BagCraftingCategory(registry.getJeiHelpers.getGuiHelper))
  }

  override def register(registry: IModRegistry): Unit = {
    val recipesWorkbench = new ListBuffer[BagCraftingRecipe]
    BagRecipe.recipes.foreach(map => recipesWorkbench += new BagCraftingRecipe(map._1, map._2))
    registry.addRecipes(recipesWorkbench.toList.asJavaCollection, BagCraftingCategory.UID)
    registry.addRecipeCatalyst(new ItemStack(BagBlocks.UPDATOR), BagCraftingCategory.UID)
    //***********************************
    val recipesInfuser = new ListBuffer[BagInfuserRecipe]
    BagItems.list.filter(bag => bag.isInstanceOf[ItemBagBase]).foreach(bag =>
      BagItems.list.filter(updater => updater.isInstanceOf[ItemUpdateBase]).foreach(updater=>
        recipesInfuser += new BagInfuserRecipe(damage(new ItemStack(bag)),new ItemStack(updater))))
    registry.addRecipes(recipesInfuser.toList.asJavaCollection, BagInfuserCategory.UID)
  }

  def damage(stack: ItemStack): ItemStack = {
    stack.setItemDamage(stack.getMaxDamage / 3)
    stack
  }
}
