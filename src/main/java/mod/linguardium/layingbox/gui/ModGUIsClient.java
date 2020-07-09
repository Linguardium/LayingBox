package mod.linguardium.layingbox.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class ModGUIsClient {
    public static void init() {
        ScreenRegistry.<PelletGuiDescription, PelletScreen>register(ModGUIs.PELLET_SCREEN_HANDLER_TYPE, (gui, inventory, title) -> new PelletScreen(gui, inventory.player, title));
    }

}
