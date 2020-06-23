package mod.linguardium.layingbox;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import com.swordglowsblue.artifice.impl.ArtificeResourcePackImpl;
import mod.linguardium.layingbox.config.ChickenConfigs;
import mod.linguardium.layingbox.entity.ModEntities;
import mod.linguardium.layingbox.render.LayingBoxRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.resource.ResourceType;

import static mod.linguardium.layingbox.blocks.ModBlocks.LAYING_BOX_ENTITY;
@Environment(EnvType.CLIENT)
public class LayingBoxMainClient implements ClientModInitializer {
    static {
        ArtificeResourcePack pack = new ArtificeResourcePackImpl(ResourceType.CLIENT_RESOURCES,(a)->{});
    }
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(LAYING_BOX_ENTITY, LayingBoxRenderer::new);
        ModEntities.initClient();
        ChickenConfigs.registerAssets();
    }
}
