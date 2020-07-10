package mod.linguardium.layingbox.compat.patchouli;

import net.minecraft.util.Identifier;
import vazkii.patchouli.client.book.ClientBookRegistry;
import vazkii.patchouli.common.base.Patchouli;

public class PatchouliConfig {
    public static void init() {
        ClientBookRegistry.INSTANCE.pageTypes.putIfAbsent(new Identifier(Patchouli.MOD_ID, "chicken_entity"), ChickenBreedingPage.class);
    }
}
