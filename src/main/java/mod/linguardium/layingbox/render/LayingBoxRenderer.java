package mod.linguardium.layingbox.render;

import mod.linguardium.layingbox.blocks.blockentity.LayingBoxEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class LayingBoxRenderer extends BlockEntityRenderer<LayingBoxEntity>{

    public LayingBoxRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(LayingBoxEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MobEntity e = entity.displayChicken;
        if (e!=null) {
            BlockPos pos = entity.getPos();
            Direction dir = entity.getWorld().getBlockState(pos).get(HorizontalFacingBlock.FACING);
            int lightLevel = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().offset(dir));
/*            LivingEntity playerEntity = entity.getWorld().getClosestPlayer(pos.getX(),pos.getY(),pos.getZ(),8,player->{
                if (player instanceof PlayerEntity) {
                    return !player.isInvulnerable() && !player.isSneaking();
                }
                return false;
            });*/
            EntityRenderer eRenderer = MinecraftClient.getInstance().getEntityRenderManager().getRenderer(e);
            e.resetPosition(pos.getX(),pos.getY(),pos.getZ());
            e.bodyYaw=dir.asRotation();
            e.headYaw=dir.asRotation();
            e.pitch=0;
            e.limbDistance=-1.5f;
            e.limbAngle=0;
            matrices.push();
            matrices.translate(0.5,-e.getHeightOffset(),0.5);
            /*if (playerEntity != null)
                e.getLookControl().lookAt(playerEntity,1.0f,1.0f);

            else
                e.getLookControl().lookAt(new Vec3d(pos.getX(),pos.getY()+0.5,pos.getZ()).add(dir.getOffsetX(),dir.getOffsetY(),dir.getOffsetZ()));
            e.getLookControl().tick();*/
            eRenderer.render(e,0.0f,1.0f,matrices,vertexConsumers,lightLevel);
            matrices.pop();
            //MinecraftClient.getInstance().getEntityRenderManager().render(e,pos.getX(),pos.getY()+2.0f,pos.getZ(),1.0f,1.0f,matrices,vertexConsumers,lightAbove);
        }
    }
}
