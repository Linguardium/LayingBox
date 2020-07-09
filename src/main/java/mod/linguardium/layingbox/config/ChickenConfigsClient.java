package mod.linguardium.layingbox.config;

import mod.linguardium.layingbox.entity.ResourceChicken;
import mod.linguardium.layingbox.render.PelletColorProvider;
import mod.linguardium.layingbox.render.ResourceChickenRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

public class ChickenConfigsClient {
    @Environment(EnvType.CLIENT)
    public static void RegisterRenderer(EntityType<ResourceChicken> chicken) {
        EntityRendererRegistry.INSTANCE.register(chicken, (entityRenderDispatcher, context) -> new ResourceChickenRenderer(entityRenderDispatcher));
    }
    @Environment(EnvType.CLIENT)
    public static void RegisterRenderer(Item item) {
        ColorProviderRegistry.ITEM.register(PelletColorProvider.INSTANCE,item);
    }
}
