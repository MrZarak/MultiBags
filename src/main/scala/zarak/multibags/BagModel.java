package zarak.multibags;


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class BagModel extends ModelBase {
        public ModelRenderer Main;
        public ModelRenderer Neck;
        public ModelRenderer Top;

        public BagModel() {
            this.textureWidth = 32;
            this.textureHeight = 32;
            this.Neck = new ModelRenderer(this, 0, 16);
            this.Neck.setRotationPoint(1.0F, 0.5F, -1.6F);
            this.Neck.addBox(-2.0F, -1.0F, -0.5F, 6, 4, 2, 0.0F);
            this.Main = new ModelRenderer(this, 0, 0);
            this.Main.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.Main.addBox(-2.0F, -1.5F, -1.0F, 8, 6, 3, 0.0F);
            this.Top = new ModelRenderer(this, 0, 10);
            this.Top.setRotationPoint(0.0F, -1.0F, 0.0F);
            this.Top.addBox(-2.0F, -2.5F, -1.0F, 8, 2, 3, 0.0F);
            this.Main.addChild(this.Neck);
            this.Main.addChild(this.Top);
        }

        @Override
        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            this.Main.render(f5);
        }

        public void render() {
            this.Main.render(1);
        }
}
