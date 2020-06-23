package mod.linguardium.layingbox.render;


import mod.linguardium.layingbox.entity.ResourceChicken;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static mod.linguardium.layingbox.LayingBoxMain.MOD_ID;


public class ResourceChickenMarkingRenderer extends FeatureRenderer<ResourceChicken, ChickenEntityModel<ResourceChicken>> {
    private static final Identifier MARKINGS = new Identifier(MOD_ID,"textures/entity/chicken/markings.png");
    private static final Identifier SKIN = new Identifier("textures/entity/chicken.png");
    private final ChickenEntityModel<ResourceChicken> model = new ChickenEntityModel<>();
    public ResourceChickenMarkingRenderer(FeatureRendererContext<ResourceChicken, ChickenEntityModel<ResourceChicken>> context) {
        super(context);
    }
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, ResourceChicken entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.isInvisible()) {
            int color = entity.base_color;
            float r = (color>>16 & 0xFF) / 255.0f;
            float g = (color>>8 & 0xFF)/ 255.0f;
            float b = (color & 0xFF) / 255.0f;
            Identifier baseSkin = (entity.texture!=null)?entity.texture:SKIN;
            render(this.getContextModel(), this.model, baseSkin, matrixStack, vertexConsumerProvider, light, entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch,tickDelta, r,g,b);
            color = entity.accent_color;
            r = (color>>16 & 0xFF) / 255.0f;
            g = (color>>8 & 0xFF)/ 255.0f;
            b = (color & 0xFF) / 255.0f;
            Identifier accentSkin = (entity.accentTexture!=null)?entity.accentTexture:MARKINGS;
            render(this.getContextModel(), this.model, accentSkin, matrixStack, vertexConsumerProvider, light, entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch,tickDelta, r,g,b);
        }
    }
}
