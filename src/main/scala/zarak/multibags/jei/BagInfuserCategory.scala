package zarak.multibags.jei

import javax.annotation.Nullable
import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.{IDrawableAnimated, IDrawableStatic, IRecipeLayout}
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import zarak.multibags.api.Reference

class BagInfuserCategory(h: IGuiHelper) extends IRecipeCategory[BagInfuserRecipe] {

  var bg: IDrawableStatic = h.createDrawable(new ResourceLocation(Reference.ID, "textures/gui/bag_repair_jei.png"), 0, 0, 352 / 2, 332 / 2)
  var statically: IDrawableStatic = h.createDrawable(new ResourceLocation(Reference.ID, "textures/gui/bag_repair_jei.png"), 177, 5, 16, 16)
  var animated: IDrawableAnimated = h.createAnimatedDrawable(statically, 40, IDrawableAnimated.StartDirection.LEFT, false)


  override def getUid: String = BagInfuserCategory.UID


  override def drawExtras(minecraft: Minecraft): Unit = animated.draw(minecraft, 128, 33)


  override def getTitle: String = I18n.format("jei.infuser.category")


  override def getModName: String = Reference.NAME


  override def getBackground: IDrawableStatic = bg


  @Nullable override def getIcon: IDrawableStatic = statically



  def setRecipe(layout: IRecipeLayout, recipes: BagInfuserRecipe, ingredients: IIngredients): Unit = {
    val isg = layout.getItemStacks
    isg.init(0, true, 126, 14)
    isg.set(0, recipes.bag)
    isg.init(1, true, 126, 50)
    isg.set(1, recipes.update)
    isg.init(2, false, 99, 49)
    isg.set(2, recipes.output)
  }
}
object BagInfuserCategory{
  val UID: String = Reference.ID + ":bag_infuser"
}