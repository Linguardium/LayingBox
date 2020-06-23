package mod.linguardium.layingbox.render;


import mod.linguardium.layingbox.entity.ResourceChicken;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ResourceChickenRenderer extends MobEntityRenderer<ResourceChicken, ChickenEntityModel<ResourceChicken>> {
    private static final Identifier SKIN = new Identifier("textures/entity/chicken.png");

    public ResourceChickenRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new ChickenEntityModel<>(), 0.3F);
        this.addFeature(new ResourceChickenMarkingRenderer(this));
    }
    public Identifier getTexture(ResourceChicken chickenEntity) {
        return SKIN;
    }

    protected float getAnimationProgress(ResourceChicken chickenEntity, float f) {
        float g = MathHelper.lerp(f, chickenEntity.prevFlapProgress, chickenEntity.flapProgress);
        float h = MathHelper.lerp(f, chickenEntity.prevMaxWingDeviation, chickenEntity.maxWingDeviation);
        return (MathHelper.sin(g) + 1.0F) * h;
    }

}
