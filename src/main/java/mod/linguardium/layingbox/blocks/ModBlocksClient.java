package mod.linguardium.layingbox.blocks;

import mod.linguardium.layingbox.render.LayingBoxRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

import static mod.linguardium.layingbox.blocks.ModBlocks.*;

@Environment(EnvType.CLIENT)
public class ModBlocksClient {
    public static void init() {
        BlockEntityRendererRegistry.INSTANCE.register(LAYING_BOX_ENTITY, LayingBoxRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(PELLETIZER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AUTOMATIC_PELLETIZER, RenderLayer.getCutout());
    }
}
