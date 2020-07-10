package mod.linguardium.layingbox;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import com.swordglowsblue.artifice.impl.ArtificeResourcePackImpl;
import mod.linguardium.layingbox.blocks.ModBlocksClient;
import mod.linguardium.layingbox.compat.patchouli.PatchouliConfig;
import mod.linguardium.layingbox.config.ChickenConfigs;
import mod.linguardium.layingbox.entity.ModEntitiesClient;
import mod.linguardium.layingbox.gui.ModGUIsClient;
import mod.linguardium.layingbox.render.PelletColorProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;

import static mod.linguardium.layingbox.items.ModItems.DEFAULT_PELLET;

@Environment(EnvType.CLIENT)
public class LayingBoxMainClient implements ClientModInitializer {
    static {
        ArtificeResourcePack pack = new ArtificeResourcePackImpl(ResourceType.CLIENT_RESOURCES,(a)->{});
    }
    @Override
    public void onInitializeClient() {
        ModBlocksClient.init();
        ModEntitiesClient.initClient();
        ChickenConfigs.registerAssets();
        ModGUIsClient.init();
        ColorProviderRegistry.ITEM.register(PelletColorProvider.INSTANCE,DEFAULT_PELLET);
        if (FabricLoader.getInstance().isModLoaded("patchouli")) {
            PatchouliConfig.init();
        }
    }
}
