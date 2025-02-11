package net.ltxprogrammer.changed.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ltxprogrammer.changed.client.renderer.model.LatexHumanoidModel;
import net.ltxprogrammer.changed.entity.LatexEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class LatexGelLayer<T extends LatexEntity, M extends LatexHumanoidModel<T>> extends RenderLayer<T, M> {
    private final LatexHumanoidModel<T> model;

    public LatexGelLayer(RenderLayerParent<T, M> p_174536_, M model) {
        super(p_174536_);
        this.model = model;
    }

    public void render(PoseStack p_117470_, MultiBufferSource p_117471_, int p_117472_, T p_117473_, float p_117474_, float p_117475_, float p_117476_, float p_117477_, float p_117478_, float p_117479_) {
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = minecraft.shouldEntityAppearGlowing(p_117473_) && p_117473_.isInvisible();
        if (!p_117473_.isInvisible() || flag) {
            VertexConsumer vertexconsumer;
            if (flag) {
                vertexconsumer = p_117471_.getBuffer(RenderType.outline(this.getTextureLocation(p_117473_)));
            } else {
                vertexconsumer = p_117471_.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(p_117473_)));
            }

            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(p_117473_, p_117474_, p_117475_, p_117476_);
            this.model.setupAnim(p_117473_, p_117474_, p_117475_, p_117477_, p_117478_, p_117479_);
            this.model.renderToBuffer(p_117470_, vertexconsumer, p_117472_, LivingEntityRenderer.getOverlayCoords(p_117473_, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public LatexHumanoidModel<T> getModel() {
        return model;
    }
}
