package mod.linguardium.layingbox.items;

import net.minecraft.item.Item;

public class PelletItem extends Item {
    public final int color1;
    public final int color2;
    public final int color3;

    public PelletItem(int color1, int color2, int color3, Settings settings) {
        super(settings);
        this.color1=color1;
        this.color2=color2;
        this.color3=color3;
    }
}
