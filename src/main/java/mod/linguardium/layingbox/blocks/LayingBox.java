package mod.linguardium.layingbox.blocks;

import mod.linguardium.layingbox.blocks.blockentity.LayingBoxEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class LayingBox extends HorizontalFacingBlock implements InventoryProvider, BlockEntityProvider{

    public LayingBox(Settings settings) {
        super(settings);
        setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
        if (blockEntity instanceof LayingBoxEntity) {
            MobEntity e;
            while ((e = ((LayingBoxEntity) blockEntity).removeChicken()) != null) {
                e.updatePosition(pos.getX()+0.5,pos.getY()+0.25,pos.getZ()+0.5);
                world.spawnEntity(e);
            }
        }
        super.afterBreak(world, player, pos, state, blockEntity, stack);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
       // return super.getRenderType(state);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new LayingBoxEntity();
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof LayingBoxEntity) {
            return ((LayingBoxEntity) be).inventory;
        }
        return null;
    }
}
