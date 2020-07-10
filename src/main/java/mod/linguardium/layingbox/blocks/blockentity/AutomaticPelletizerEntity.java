package mod.linguardium.layingbox.blocks.blockentity;

import mod.linguardium.layingbox.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class AutomaticPelletizerEntity extends PelletizerEntity implements Tickable {

    public static final int TICK_INTERVAL = 20;
    protected int tickCount;
    public AutomaticPelletizerEntity() {
        super(ModBlocks.AUTOMATIC_PELLETIZER_ENTITY);
        tickCount=0;
    }
    @Override
    public void onRedstoneRising() {
        //stubbed
    }
    @Override
    public void tick() {
        if (world.isClient())
            return;
        tickCount++;
        if (tickCount<TICK_INTERVAL)
            return;
        tickCount=0;
        if (ticksTotal>0)
            sync();
        if (recipe != null && --ticksRemaining<=0) {
            if (canOutput()) {
                dropPellet();
            }else{
                ticksRemaining=1;
            }
        }
        if (ticksRemaining < 1) {
            ticksRemaining = ticksTotal = setupNewRecipe();
        }
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("tickCount",tickCount);
        return tag;
    }

    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        tickCount = tag.getInt("tickCount");
    }

}
