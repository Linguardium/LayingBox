package mod.linguardium.layingbox.api;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import java.util.stream.IntStream;

public class BasicInventory extends SimpleInventory implements SidedInventory {

    public BasicInventory(int size) {
        super(size);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return IntStream.range(0,size()).toArray();
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}
