package zarak.multibags.jei

import javax.annotation.Nullable
import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.{IDrawableStatic, IRecipeLayout}
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import zarak.multibags.api.Reference

class BagCraftingCategory(h: IGuiHelper) extends IRecipeCategory[BagCraftingRecipe] {

  var bg: IDrawableStatic = h.createDrawable(new ResourceLocation(Reference.ID, "textures/gui/bag_repair_jei.png"), 0, 0, 352 / 2, 332 / 2)

  override def getUid: String = BagCraftingCategory.UID


  override def getTitle: String = I18n.format("jei.workbench.category")


  override def getModName: String = Reference.NAME


  @Nullable override def getBackground: IDrawableStatic = bg


  def setRecipe(layout: IRecipeLayout, recipes: BagCraftingRecipe, ingredients: IIngredients): Unit = {
    val isg = layout.getItemStacks
    recipes.items.zipWithIndex.foreach(itemIndex => {
      isg.init(itemIndex._2, true, 32 + (itemIndex._2 % 3) * 20, 12 + (itemIndex._2 / 3) * 20)
      isg.set(itemIndex._2, itemIndex._1)
    })
    isg.init(recipes.items.length, false, 99, 15)
    isg.set(recipes.items.length, recipes.result)
  }
}

object BagCraftingCategory {
  val UID: String = Reference.ID + ":bag_workbench"
}