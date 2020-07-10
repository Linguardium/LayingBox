package mod.linguardium.layingbox.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class PelletGuiDescription extends SyncedGuiDescription {
    public static final Identifier FULL_BAR = new Identifier("layingbox:textures/gui/horizontal_bar_full.png");
    public static final Identifier EMPTY_BAR = new Identifier("layingbox:textures/gui/horizontal_bar_empty.png");
    public PelletGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModGUIs.PELLET_SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, 2), getBlockPropertyDelegate(context,3));
        WGridPanel root = new WGridPanel();

        setRootPanel(root);
        root.setSize(150, 100);
        WItemSlot ingredientSlot = WItemSlot.of(blockInventory, 0);
        root.add(ingredientSlot, 4, 1);
        WItemSlot seedSlot = WItemSlot.of(blockInventory, 1);
        root.add(seedSlot, 4, 2);
        //int remaining = propertyDelegate.get(0);
        //int total = propertyDelegate.get(1);
        WBar progress = new WBar(EMPTY_BAR,FULL_BAR,2,1, WBar.Direction.RIGHT);
        root.add(progress,3,3,3,1);
        //WSprite seedimage = new WSprite(new Identifier("textures/item/wheat_seeds.png"));
        //seedimage.setTint(0xA0A0A0);
        //root.add(seedimage,6,1);
        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }
}
