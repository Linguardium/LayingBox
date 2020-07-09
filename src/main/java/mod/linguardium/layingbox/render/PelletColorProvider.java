package mod.linguardium.layingbox.render;

import mod.linguardium.layingbox.items.PelletItem;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemStack;

public class PelletColorProvider implements ItemColorProvider {
    public static PelletColorProvider INSTANCE = new PelletColorProvider();

    private PelletColorProvider() {}
    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (stack.getItem() instanceof PelletItem) {
            if (tintIndex==1)
                return ((PelletItem) stack.getItem()).color2;
            if (tintIndex==2)
                return ((PelletItem) stack.getItem()).color3;
            return ((PelletItem) stack.getItem()).color1;
        }
        return 0;
    }
}
