package mod.linguardium.layingbox.entity;

import mod.linguardium.layingbox.render.ResourceChickenRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

import static mod.linguardium.layingbox.entity.ModEntities.RESOURCE_CHICKEN_TYPE;

public class ModEntitiesClient {
    @Environment(EnvType.CLIENT)
    public static void initClient() {
        EntityRendererRegistry.INSTANCE.register(RESOURCE_CHICKEN_TYPE, (entityRenderDispatcher, context) -> new ResourceChickenRenderer(entityRenderDispatcher));
    }
}
