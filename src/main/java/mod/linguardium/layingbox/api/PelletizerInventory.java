package mod.linguardium.layingbox.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import static mod.linguardium.layingbox.config.ChickenConfigs.chickens;
import static mod.linguardium.layingbox.items.ModItems.SEEDS_TAG;

public class PelletizerInventory extends BasicInventory {
    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    public PelletizerInventory(int size) {
        super(size);
    }
    public PelletizerInventory(int size, DefaultedList<ItemStack> stacks) {
        super(size, stacks);
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir)
    {
        if (dir==Direction.DOWN)
            return false;
        if (slot == 1 && stack.getItem().isIn(SEEDS_TAG))
            return true;
        if (slot == 0 && chickens.values().stream().anyMatch(c->c.dropItem.equals(Registry.ITEM.getId(stack.getItem()))))
            return true;
        return false;
    }

}
