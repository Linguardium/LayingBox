package mod.linguardium.layingbox.entity;

import mod.linguardium.layingbox.api.LayingBoxProvider;
import mod.linguardium.layingbox.api.ResourceChickenConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

import static mod.linguardium.layingbox.config.ChickenConfigs.chickens;
import static mod.linguardium.layingbox.entity.ModEntities.RESOURCE_CHICKEN_TYPE;

public class ResourceChicken extends ChickenEntity implements LayingBoxProvider<ResourceChicken> {
    public final int base_color;
    public final int accent_color;
    public final Identifier dropItem;
    public final Identifier texture;
    public final Identifier accentTexture;
    public ResourceChicken(World world) {
        this(RESOURCE_CHICKEN_TYPE, world,-1,-1,null,null,null);
    }

    public ResourceChicken(EntityType<ResourceChicken> type, World world, int base_color, int accent_color, Identifier item, Identifier texture, Identifier accentTexture) {
        super(type, world);
        this.base_color=base_color;
        this.accent_color=accent_color;
        this.dropItem = item;
        this.texture=texture;
        this.accentTexture=accentTexture;
    }

    @Override
    public ItemEntity dropItem(ItemConvertible item) {
        if (item.asItem().equals(Items.EGG)) {
            for (ItemStack stack : getDrops()) {
                dropStack(stack);
            }
        }
        return super.dropItem(item);
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> stacks = DefaultedList.of();
        //int count = 1;
        //if (this instanceof ChickenStats) {
        //    int dropadds = ((ChickenStats) this).getProduction() / 100;
        //    count = 1 +((dropadds>0)?world.getRandom().nextInt(dropadds):0);
        //}
        stacks.add(new ItemStack(Registry.ITEM.get(dropItem)));
        if (getEntityWorld().getRandom().nextFloat() <= 0.1) {
            stacks.add(new ItemStack(Items.FEATHER));
        }
        return stacks;
    }

    @Override
    protected void initGoals() {
            this.goalSelector.add(0, new SwimGoal(this));
            this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4D));
            this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
            this.goalSelector.add(4, new FollowParentGoal(this, 1.1D));
            this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
            this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
            this.goalSelector.add(7, new LookAroundGoal(this));
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        ResourceChickenConfig c = chickens.get(this.getType());
        if (c!=null) {
            return stack.getItem().equals(Registry.ITEM.get(c.feed));
        }
        return super.isBreedingItem(stack);
    }

}
