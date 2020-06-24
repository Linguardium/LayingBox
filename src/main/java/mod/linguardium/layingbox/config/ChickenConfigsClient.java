package mod.linguardium.layingbox.config;

import mod.linguardium.layingbox.entity.ResourceChicken;
import mod.linguardium.layingbox.render.ResourceChickenRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;

public class ChickenConfigsClient {
    @Environment(EnvType.CLIENT)
    public static void RegisterRenderer(EntityType<ResourceChicken> chicken) {
        EntityRendererRegistry.INSTANCE.register(chicken, (entityRenderDispatcher, context) -> new ResourceChickenRenderer(entityRenderDispatcher));
    }
}
