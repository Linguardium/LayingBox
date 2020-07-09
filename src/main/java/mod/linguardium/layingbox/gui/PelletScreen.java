package mod.linguardium.layingbox.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class PelletScreen extends CottonInventoryScreen<PelletGuiDescription> {
    public PelletScreen(PelletGuiDescription gui, PlayerEntity player, Text title) {
        super(gui, player, title);
    }
}
