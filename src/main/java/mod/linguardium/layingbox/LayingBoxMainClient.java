package mod.linguardium.layingbox;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import com.swordglowsblue.artifice.impl.ArtificeResourcePackImpl;
import mod.linguardium.layingbox.blocks.ModBlocksClient;
import mod.linguardium.layingbox.config.ChickenConfigs;
import mod.linguardium.layingbox.entity.ModEntitiesClient;
import mod.linguardium.layingbox.gui.ModGUIsClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourceType;
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
    }
}
