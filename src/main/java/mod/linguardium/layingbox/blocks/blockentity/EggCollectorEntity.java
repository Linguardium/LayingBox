package mod.linguardium.layingbox.blocks.blockentity;

import mod.linguardium.layingbox.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Nameable;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class EggCollectorEntity extends LootableContainerBlockEntity implements Nameable, Tickable {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(54,ItemStack.EMPTY);
    int tickCount = 0;
    public EggCollectorEntity() {
        super(ModBlocks.EGG_COLLECTOR_ENTITY);
    }

    @Override
    public DefaultedList<ItemStack> getInvStackList() {
        return inventory;
    }

    @Override
    public void setInvStackList(DefaultedList<ItemStack> list) {
        inventory = list;
    }

    @Override
    public Text getContainerName() {
        return new TranslatableText("container.layingbox.egg_collector");
    }

    @Override
    public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return GenericContainerScreenHandler.createGeneric9x6(syncId,playerInventory,this);
    }

    @Override
    public int size() {
        return 54;
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public void tick() {
        if (world.isClient())
            return;
        tickCount++;
        if (tickCount>=20) {
            tickCount=0;
            int x1 = pos.getX()-8;
            int y1=pos.getY()-8;
            int z1=pos.getZ()-8;
            for (int x = x1;x<x1+16;x++) {
                for (int y = y1;y<y1+16;y++) {
                    for (int z = z1;z<z1+16;z++) {
                        BlockEntity be = world.getBlockEntity(new BlockPos(x,y,z));
                        if (be instanceof LayingBoxEntity && !((LayingBoxEntity) be).inventory.isEmpty()) {
                            Inventory inv = ((LayingBoxEntity) be).inventory;
                            for (int i: ((LayingBoxEntity) be).inventory.getAvailableSlots(Direction.DOWN)) {
                                if (canInsert(inv.getStack(i))) {
                                    inv.setStack(i,addStack(inv.getStack(i)));
                                }
                            }
                        }
                    }

                }

            }
        }
    }

    public boolean canInsert(ItemStack stack) {
        for (ItemStack itemStack : inventory) {
            if (itemStack.isEmpty() || this.canCombine(itemStack, stack) && itemStack.getCount() < itemStack.getMaxCount()) {
                return true;
            }
        }
        return false;
    }
    private boolean canCombine(ItemStack one, ItemStack two) {
        return one.getItem() == two.getItem() && ItemStack.areTagsEqual(one, two);
    }

    private void transfer(ItemStack source, ItemStack target) {
        int i = Math.min(this.getMaxCountPerStack(), target.getMaxCount());
        int j = Math.min(source.getCount(), i - target.getCount());
        if (j > 0) {
            target.increment(j);
            source.decrement(j);
            this.markDirty();
        }

    }
    public ItemStack addStack(ItemStack stack) {
        ItemStack itemStack = stack.copy();
        this.addToExistingSlot(itemStack);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.addToNewSlot(itemStack);
            return itemStack.isEmpty() ? ItemStack.EMPTY : itemStack;
        }
    }
    private void addToNewSlot(ItemStack stack) {
        for(int i = 0; i < size(); ++i) {
            ItemStack itemStack = this.getStack(i);
            if (itemStack.isEmpty()) {
                this.setStack(i, stack.copy());
                stack.setCount(0);
                return;
            }
        }

    }

    private void addToExistingSlot(ItemStack stack) {
        for(int i = 0; i < size(); ++i) {
            ItemStack itemStack = this.getStack(i);
            if (this.canCombine(itemStack, stack)) {
                this.transfer(stack, itemStack);
                if (stack.isEmpty()) {
                    return;
                }
            }
        }

    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, this.inventory);

        return tag;
    }

    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.fromTag(tag, this.inventory);

    }
}
