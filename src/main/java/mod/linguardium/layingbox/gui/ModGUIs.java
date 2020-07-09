package mod.linguardium.layingbox.gui;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import static mod.linguardium.layingbox.LayingBoxMain.MOD_ID;

public class ModGUIs {

    public static ScreenHandlerType<PelletGuiDescription> PELLET_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID,"pelletizer"), (syncId, inventory) -> new PelletGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY));
    public static void init() {

    }

}
