package mod.linguardium.layingbox.items;

import mod.linguardium.layingbox.blocks.LayingBox;
import mod.linguardium.layingbox.blocks.blockentity.LayingBoxEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class NettedAnimalItem extends Item {

    public NettedAnimalItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        BlockPos pos = context.getBlockPos();
        BlockState state = context.getWorld().getBlockState(pos);
        if (state.getBlock() instanceof LayingBox) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof LayingBoxEntity) {
                if (((LayingBoxEntity) be).addChicken(context.getStack())) {
                    context.getStack().decrement(1);
                    return ActionResult.SUCCESS;
                }else{
                    return ActionResult.FAIL;
                }
            }
        }
        CompoundTag tag = context.getStack().getOrCreateSubTag("EntityTag");
        if (!tag.isEmpty()) {
            EntityType type= EntityType.get(tag.getString("id")).orElse(null);
            if (type!=null) {
                //Vec3d vPos = context.getHitPos();
                Entity e = type.spawnFromItemStack(world, context.getStack(), context.getPlayer(),new BlockPos(context.getHitPos()), SpawnReason.NATURAL, false, false);
                //e.resetPosition(vPos.getX(),vPos.getY(),vPos.getZ());
                if (e!=null) {
                    context.getStack().decrement(1);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        CompoundTag eTag = stack.getOrCreateSubTag("EntityTag");
        if (!eTag.isEmpty()) {
            Text name = null;
            if (eTag.contains("CustomName", NbtType.STRING)) {
                name=Text.Serializer.fromJson(eTag.getString("CustomName"));
            }
            EntityType type = EntityType.get(eTag.getString("id")).orElse(null);
            if (type!=null) {
                if (name!=null) {
                    name=new TranslatableText("item.layingbox.netted_animal.customid",name,type.getName());
                }else{
                    name = type.getName();
                }
            }
            if (name!=null)
                tooltip.add( new TranslatableText("item.layingbox.netted_animal.id",name));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Text getName(ItemStack stack) {
        return super.getName(stack);
    }
}
