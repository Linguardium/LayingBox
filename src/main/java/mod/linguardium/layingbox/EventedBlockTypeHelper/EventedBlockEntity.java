package mod.linguardium.layingbox.EventedBlockTypeHelper;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class EventedBlockEntity extends BlockEntity implements Tickable {

    private Object2IntArrayMap<Direction> redstoneValues = new Object2IntArrayMap<>();

    public EventedBlockEntity(BlockEntityType<?> type) {
        super(type);
    }
    private void processRedstoneState() {
        if (redstoneValues.size() < Direction.values().length) {
            updateRedstoneState();
        }
        Object2IntArrayMap<Direction> old = new Object2IntArrayMap<Direction>(redstoneValues);
        updateRedstoneState();
        boolean turnedOn = false;
        boolean turnedOff = false;
        boolean rising = false;
        boolean falling=false;
        for (Direction dir : Direction.values()) {
            int oldVal = old.getInt(dir);
            int newVal = redstoneValues.getInt(dir);
            if (newVal>oldVal) {
                rising = true;
                onRedstoneRising(dir);
                if (oldVal == 0) {
                    onRedstoneOn(dir);
                    turnedOn=true;
                }
            }else if (newVal < oldVal) {
                falling=true;
                onRedstoneFalling(dir);
                if (newVal==0) {
                    onRedstoneOff(dir);
                    turnedOff=true;
                }
            }
            if (newVal!=oldVal) {
                onRedstoneChanged(dir);
            }
        }
        if (rising) {
            onRedstoneRising();
            if (turnedOn) {
                onRedstoneOn();
            }
        }else if (falling) {
            onRedstoneFalling();
            if (turnedOff) {
                onRedstoneOff();
            }
        }
        if (rising || falling) {
            onRedstoneChanged();
        }
    }
    public void onRedstoneOn() {}
    public void onRedstoneOff() {}
    public void onRedstoneChanged() {}
    public void onRedstoneRising() {}
    public void onRedstoneFalling() {}
    public void onRedstoneChanged(Direction dir){}
    public void onRedstoneOn(Direction dir) {}
    public void onRedstoneOff(Direction dir) {}
    public void onRedstoneRising(Direction dir) {}
    public void onRedstoneFalling(Direction dir) {}
    private void updateRedstoneState() {
        for (Direction dir : Direction.values()) {
            BlockPos blockPos = pos.offset(dir);
            int redstone = world.getEmittedRedstonePower(blockPos, dir);
            redstoneValues.put(dir,redstone);
        }



       /*if (i >= 15) {
            return i;
        } else {
            BlockState blockState = world.getBlockState(blockPos);
            return Math.max(i, blockState.isOf(Blocks.REDSTONE_WIRE) ? (Integer)blockState.get(RedstoneWireBlock.POWER) : 0);
        }*/

    }


    @Override
    public void tick() {
        if (this instanceof RedstoneListener) {
            processRedstoneState();
        }
    }

    public interface RedstoneListener {}
}
