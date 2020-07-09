package mod.linguardium.layingbox.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class PelletGuiDescription extends SyncedGuiDescription {
    public PelletGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModGUIs.PELLET_SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, 2), getBlockPropertyDelegate(context));
        WGridPanel root = new WGridPanel();

        setRootPanel(root);
        root.setSize(150, 100);
        WItemSlot ingredientSlot = WItemSlot.of(blockInventory, 0);
        root.add(ingredientSlot, 4, 1);
        WItemSlot seedSlot = WItemSlot.of(blockInventory, 1);
        root.add(seedSlot, 4, 2);
        //WSprite seedimage = new WSprite(new Identifier("textures/item/wheat_seeds.png"));
        //seedimage.setTint(0xA0A0A0);
        //root.add(seedimage,6,1);
        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }
}
