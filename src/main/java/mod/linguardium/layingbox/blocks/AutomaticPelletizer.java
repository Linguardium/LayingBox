package mod.linguardium.layingbox.blocks;

import mod.linguardium.layingbox.blocks.blockentity.AutomaticPelletizerEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class AutomaticPelletizer extends Pelletizer {
    public AutomaticPelletizer(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new AutomaticPelletizerEntity();
    }

}
