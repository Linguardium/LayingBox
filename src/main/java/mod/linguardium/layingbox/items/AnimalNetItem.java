package mod.linguardium.layingbox.items;

import mod.linguardium.layingbox.blocks.blockentity.LayingBoxEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Random;

import static mod.linguardium.layingbox.items.ModItems.NETTED_ANIMAL;
import static net.minecraft.sound.SoundEvents.ENTITY_GENERIC_SMALL_FALL;

public class AnimalNetItem extends Item {

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient())
            return ActionResult.SUCCESS;
        BlockPos pos = context.getBlockPos();
        BlockEntity be = world.getBlockEntity(pos);
        BlockState state = world.getBlockState(pos);
        if (be instanceof LayingBoxEntity) {
            MobEntity entity = ((LayingBoxEntity) be).removeChicken();
            if (entity!= null) {
                ServerPlayerEntity user = (ServerPlayerEntity) context.getPlayer();
                ItemStack stack = context.getStack();
                Hand hand = context.getHand();
                CompoundTag tag = entity.toTag(new CompoundTag());
                ItemStack netted_stack = new ItemStack(NETTED_ANIMAL);
                tag.putString("id", EntityType.getId(entity.getType()).toString());
                tag.remove("UUID");
                tag.remove("Pos");
                netted_stack.getOrCreateTag().put("EntityTag", tag);
                if (!user.giveItemStack(netted_stack)) {
                    ItemScatterer.spawn(user.world, user.getX(), user.getY(), user.getZ(), netted_stack);
                }
                stack.damage(1, user, p -> p.sendToolBreakStatus(hand));
                return ActionResult.SUCCESS;
            }else{
                return ActionResult.FAIL;
            }
        }
        return super.useOnBlock(context);
    }

    public AnimalNetItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof PassiveEntity) {
            if (!user.world.isClient()) {

                CompoundTag tag = entity.toTag(new CompoundTag());
                ItemStack netted_stack = new ItemStack(NETTED_ANIMAL);
                tag.putString("id", EntityType.getId(entity.getType()).toString());
                tag.remove("UUID");
                tag.remove("Pos");
                netted_stack.getOrCreateTag().put("EntityTag", tag);
                if (!user.giveItemStack(netted_stack)) {
                    ItemScatterer.spawn(user.world, user.getX(), user.getY(), user.getZ(), netted_stack);
                }
                stack.damage(1,user,p->p.sendToolBreakStatus(hand));
            }else{
                ((PassiveEntity)entity).playSpawnEffects();
                user.playSound(ENTITY_GENERIC_SMALL_FALL, SoundCategory.PLAYERS,1.0F,1.0F);
            }
            entity.remove();

            return ActionResult.SUCCESS;
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

}
